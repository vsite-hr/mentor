package hr.vsite.mentor.unit;

import org.apache.commons.lang3.NotImplementedException;

public class MentorTextUnit extends TextUnit {

	@Override
	public MentorTextUnitAttributes getAttributes() { return MentorTextUnitAttributes.class.cast(super.getAttributes()); }
	@Override
	public void setAttributes(Object attributes) { super.setAttributes(MentorTextUnitAttributes.class.cast(attributes)); }
	
	public String getHtml() {
		if (html == null)
			html = generateHtml();
		return html;
	}
	
	private String generateHtml() {
		switch (getAttributes().getMarkupType()) {
			case None: return getAttributes().getMarkup();
			case Markdown: return generateHtmlFromMarkdown();
		}
		throw new IllegalArgumentException("Unknown markup type: " + getAttributes().getMarkupType());
	}

	private String generateHtmlFromMarkdown() {
		// TODO implement TextUnit.generateHtmlFromMarkdown 
		throw new NotImplementedException("TextUnit.generateHtmlFromMarkdown");
	}

	private String html;	// lazy eval
	
}
