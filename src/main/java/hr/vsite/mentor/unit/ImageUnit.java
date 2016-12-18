package hr.vsite.mentor.unit;

import java.nio.file.Path;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gwt.core.shared.GwtIncompatible;

public class ImageUnit extends Unit {

	public static class Attributes {

		@JsonProperty
		public String getFilename() { return filename; }
		public void setFilename(String filename) { this.filename = filename; }
		@JsonProperty
		public String getContentType() { return contentType; }
		public void setContentType(String contentType) { this.contentType = contentType; }
		@JsonProperty
		public int getWidth() { return width; }
		public void setWidth(int width) { this.width = width; }
		@JsonProperty
		public int getHeight() { return height; }
		public void setHeight(int height) { this.height = height; }
		@JsonProperty
		public String getCaption() { return caption; }
		public void setCaption(String caption) { this.caption = caption; }

		private String filename;
		private String contentType;
		private int width;
		private int height;
		private String caption;
		
	}

	@Override
	public Attributes getAttributes() { return (Attributes) super.getAttributes(); }
	@Override
	public void setAttributes(Object attributes) { super.setAttributes((Attributes) attributes); }
	
	@Override
	@GwtIncompatible
	public Path getThumbnailPath() {
		return getImagePath();
	}

	@Override
	@GwtIncompatible
	public String getThumbnailContentType() {
		return getImageContentType();
	}

	@GwtIncompatible
	public Path getImagePath() {
		return getDataFolder().resolve(getAttributes().getFilename());
	}

	@GwtIncompatible
	public String getImageContentType() {
		return getAttributes().getContentType();
	}

}
