package hr.vsite.mentor.unit;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TextUnitAttributes {

	@JsonProperty
	public TextUnit.Type getTextUnitType() { return textUnitType; }
	public void setTextUnitType(TextUnit.Type textUnitType) { this.textUnitType = textUnitType; }

	private TextUnit.Type textUnitType;
	
}
