package hr.vsite.mentor.google;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.google.gwt.core.shared.GwtIncompatible;

import java.io.Console;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hr.vsite.mentor.MentorConfiguration;
import hr.vsite.mentor.servlet.rest.providers.ExceptionMapperProvider;

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
	
	public String GetThumbnail() {
		return targetVideo.getSnippet().getThumbnails().getDefault().getUrl();
	}
		
	public String GetDuration() {
		return VideoDuration(targetVideo.getContentDetails().getDuration());
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
	
	public String GetTitle() {
		return targetVideo.getSnippet().getTitle();
	}

	
	private String videoId;
	private List<Video> videoList;
	private Video targetVideo;
}
