package hr.vsite.mentor.unit.video;

import java.nio.file.Path;

public class MentorVideoUnit extends VideoUnit {

	@Override
	public MentorVideoUnitAttributes getAttributes() { return MentorVideoUnitAttributes.class.cast(super.getAttributes()); }
	@Override
	public void setAttributes(Object attributes) { super.setAttributes(MentorVideoUnitAttributes.class.cast(attributes)); }

	@Override
	public Path getThumbnailPath() {
		return getDataFolder().resolve(getId() + ".jpg");
	}

	public Path getVideoPath() {
		return getDataFolder().resolve(getId() + ".mp4");
	}

}
