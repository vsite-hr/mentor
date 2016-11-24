package hr.vsite.mentor.unit;

public class MentorTextUnitAttributes extends TextUnitAttributes {

	public MarkupType getMarkupType() { return markupType; }
	public void setMarkupType(MarkupType markupType) { this.markupType = markupType; }
	public String getMarkup() { return markup; }
	public void setMarkup(String markup) { this.markup = markup; }
	
	private MarkupType markupType;
	private String markup;
	
}
