package hr.vsite.mentor.unit;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MentorTextUnitAttributes extends TextUnitAttributes {

	@JsonProperty
	public MarkupType getMarkupType() { return markupType; }
	public void setMarkupType(MarkupType markupType) { this.markupType = markupType; }
	@JsonProperty
	public String getMarkup() { return markup; }
	public void setMarkup(String markup) { this.markup = markup; }
	
	private MarkupType markupType;
	private String markup;
	
}
