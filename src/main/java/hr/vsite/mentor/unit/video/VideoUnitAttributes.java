package hr.vsite.mentor.unit.video;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VideoUnitAttributes {

	@JsonProperty
	public VideoUnit.Type getVideoUnitType() { return videoUnitType; }
	public void setVideoUnitType(VideoUnit.Type videoUnitType) { this.videoUnitType = videoUnitType; }
	@JsonProperty
	public int getWidth() { return width; }
	public void setWidth(int width) { this.width = width; }
	@JsonProperty
	public int getHeight() { return height; }
	public void setHeight(int height) { this.height = height; }
	@JsonProperty
	public int getDuration() { return duration; }
	public void setDuration(int duration) { this.duration = duration; }

	private VideoUnit.Type videoUnitType;
	private int width;
	private int height;
	private int duration;

}
