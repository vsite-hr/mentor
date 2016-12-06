package hr.vsite.mentor.unit;

import java.nio.file.Path;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gwt.core.shared.GwtIncompatible;

public class VideoUnit extends Unit {

	public static class Attributes {

		@JsonProperty
		public int getWidth() { return width; }
		public void setWidth(int width) { this.width = width; }
		@JsonProperty
		public int getHeight() { return height; }
		public void setHeight(int height) { this.height = height; }
		@JsonProperty
		public int getDuration() { return duration; }
		public void setDuration(int duration) { this.duration = duration; }

		private int width;
		private int height;
		private int duration;
		
	}

	@Override
	public Attributes getAttributes() { return (Attributes) super.getAttributes(); }
	@Override
	public void setAttributes(Object attributes) { super.setAttributes((Attributes) attributes); }
	
	@Override
	@GwtIncompatible
	public Path getThumbnailPath() {
		return getDataFolder().resolve(getId() + ".jpg");
	}

	@GwtIncompatible
	public Path getVideoPath() {
		return getDataFolder().resolve(getId() + ".mp4");
	}

}
