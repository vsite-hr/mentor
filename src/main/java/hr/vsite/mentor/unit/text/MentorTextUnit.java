package hr.vsite.mentor.unit.text;

import org.apache.commons.lang3.NotImplementedException;

import com.google.gwt.core.shared.GwtIncompatible;

public class MentorTextUnit extends TextUnit {

	@Override
	public MentorTextUnitAttributes getAttributes() { return (MentorTextUnitAttributes) super.getAttributes(); }
	@Override
	public void setAttributes(Object attributes) { super.setAttributes((MentorTextUnitAttributes) attributes); }

	@GwtIncompatible
	public String getHtml() {
		if (html == null)
			html = generateHtml();
		return html;
	}
	
	@GwtIncompatible
	private String generateHtml() {
		switch (getAttributes().getMarkupType()) {
			case None: return getAttributes().getMarkup();
			case Markdown: return generateHtmlFromMarkdown();
		}
		throw new IllegalArgumentException("Unknown markup type: " + getAttributes().getMarkupType());
	}

	@GwtIncompatible
	private String generateHtmlFromMarkdown() {
		// TODO implement TextUnit.generateHtmlFromMarkdown 
		throw new NotImplementedException("TextUnit.generateHtmlFromMarkdown");
	}

	private String html;	// lazy eval
	
}
