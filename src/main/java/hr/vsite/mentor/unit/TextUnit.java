package hr.vsite.mentor.unit;

import org.apache.commons.lang3.NotImplementedException;

public class TextUnit extends Unit {

	public MarkupType getMarkupType() { return markupType; }
	public void setMarkupType(MarkupType markupType) { this.markupType = markupType; }
	public String getMarkup() { return markup; }
	public void setMarkup(String markup) { this.markup = markup; }
	
	public String getHtml() {
		if (html == null)
			html = generateHtml();
		return html;
	}
	
	private String generateHtml() {
		switch (markupType) {
			case None: return markup;
			case Markdown: return generateHtmlFromMarkdown();
		}
		throw new IllegalArgumentException("Unknown markup type: " + markupType);
	}

	private String generateHtmlFromMarkdown() {
		// TODO implement TextUnit.generateHtmlFromMarkdown 
		throw new NotImplementedException("TextUnit.generateHtmlFromMarkdown");
	}
	
	private MarkupType markupType;
	private String markup;
	private String html;	// lazy eval
	
}
