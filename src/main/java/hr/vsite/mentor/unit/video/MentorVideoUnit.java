package hr.vsite.mentor.unit.video;

import java.nio.file.Path;

import com.google.gwt.core.shared.GwtIncompatible;

public class MentorVideoUnit extends VideoUnit {

	@Override
	public MentorVideoUnitAttributes getAttributes() { return (MentorVideoUnitAttributes) super.getAttributes(); }
	@Override
	public void setAttributes(Object attributes) { super.setAttributes((MentorVideoUnitAttributes) attributes); }

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
