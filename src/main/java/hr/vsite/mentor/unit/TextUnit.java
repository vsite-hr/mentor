package hr.vsite.mentor.unit;

import org.apache.commons.lang3.NotImplementedException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gwt.core.shared.GwtIncompatible;

public class TextUnit extends Unit {

	public static class Attributes {

		@JsonProperty
		public MarkupType getMarkupType() { return markupType; }
		public void setMarkupType(MarkupType markupType) { this.markupType = markupType; }
		@JsonProperty
		public String getMarkup() { return markup; }
		public void setMarkup(String markup) { this.markup = markup; }
		
		private MarkupType markupType;
		private String markup;
		
	}

	public static enum MarkupType {
		None,
		Markdown
	}

	@Override
	public Attributes getAttributes() { return (Attributes) super.getAttributes(); }
	@Override
	public void setAttributes(Object attributes) { super.setAttributes((Attributes) attributes); }

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
