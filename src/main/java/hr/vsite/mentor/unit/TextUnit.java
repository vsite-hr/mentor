package hr.vsite.mentor.unit;

public abstract class TextUnit extends Unit {

	public static enum Type {
		Mentor,
		GoogleDoc,
		Open365
	}

	@Override
	public TextUnitAttributes getAttributes() { return TextUnitAttributes.class.cast(super.getAttributes()); }
	@Override
	public void setAttributes(Object attributes) { super.setAttributes(TextUnitAttributes.class.cast(attributes)); }
	
}
