package hr.vsite.mentor.google;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.google.gwt.core.shared.GwtIncompatible;


import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hr.vsite.mentor.MentorConfiguration;


public class YoutubeManager {

	public YoutubeManager(String id) {
		super();
		videoId = GetId(id);
		
		try {
			Connect();
		} catch (IOException e) {
			// TODO What to throw?
			e.printStackTrace();
		}
	}
	// TODO What to do/throw when pattern is not recognized? 
    static String GetId(String url)
    {
		String pattern = "(?<=youtu.be/|watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
	    Pattern compiledPattern = Pattern.compile(pattern);
		Matcher matcher = compiledPattern.matcher(url);
		matcher.find();			
		return matcher.group();
    }
    
    
    @GwtIncompatible
    public static String GetApi() {
    	return MentorConfiguration.get().getYoutubeApiKey().toString();
    }  
    
	public void Connect() throws IOException {		
		YouTube youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(),
		        new HttpRequestInitializer() {
		            public void initialize(HttpRequest request) throws IOException {
		            }
		        }).setApplicationName("video-test").build();

		YouTube.Videos.List videoRequest = youtube.videos().list("snippet,statistics,contentDetails");
		videoRequest.setId(videoId);
		videoRequest.setKey(GetApi());
		VideoListResponse listResponse = videoRequest.execute();
		videoList = listResponse.getItems();		
		targetVideo = videoList.iterator().next();	
	}
	
	// Default Thumbnail = 120px by 90px
	public String GetThumbnail() {
		return targetVideo.getSnippet().getThumbnails().getDefault().getUrl();
	}
		
	public String GetDuration() {
		return VideoDuration(targetVideo.getContentDetails().getDuration());
	}
	
	public String GetTitle() {
		return targetVideo.getSnippet().getTitle();
	}
	
	// default video size 480 x 270
	public String GetIFrame () {
		int width = 480;
		int height = 270;		
		return IFrame(width, height);
	}
	
	public String GetIFrame (int width, int height) {
		return IFrame(width, height);
	}
	
	// iframe id is generated from the first word of the video title 
	private String IFrame(int width, int height) {
		
		StringBuilder IFrame = new StringBuilder(1000);
		String id = GetTitle();
		
		if (id.indexOf(' ') > -1)
			id = id.substring(0,id.indexOf(' '));
		
		IFrame.append("<iframe id=\"").append(id).append("\" ");
		IFrame.append("type=\"text/html\" width=\"").append(width).append("\" ");
		IFrame.append("hight=\"").append(height).append("\" ");
		IFrame.append("src=\"https://www.youtube.com/embed/").append(videoId).append("\" ");
		IFrame.append("frameborder=\"0\" allowfullscreen>");
		
		return IFrame.toString();
	}
	
	// Parsing video duration output. From PT5H4M13S to 5 hrs 4 min 13 sec
	private String VideoDuration(String duration) {		
		long totalSeconds = Duration.parse(duration).getSeconds();
		if (totalSeconds > 3600)
			return totalSeconds/3600 + " hrs " + (totalSeconds % 3600) / 60 + " min " + totalSeconds % 60 + " sec";
		if (totalSeconds > 60)
			return totalSeconds/ 60 + " min " + totalSeconds % 60 + " sec";
		return totalSeconds + " sec";
		}
	
	private String videoId;
	private List<Video> videoList;
	private Video targetVideo;
}
