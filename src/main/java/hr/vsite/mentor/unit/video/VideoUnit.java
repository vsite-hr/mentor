package hr.vsite.mentor.unit.video;

import hr.vsite.mentor.unit.Unit;

public abstract class VideoUnit extends Unit {

	public static enum Type {
		Mentor,
		YouTube,
		Vimeo
	}

	@Override
	public VideoUnitAttributes getAttributes() { return (VideoUnitAttributes) super.getAttributes(); }
	@Override
	public void setAttributes(Object attributes) { super.setAttributes((VideoUnitAttributes) attributes); }
	
}
