package hr.vsite.mentor.unit;

import java.io.IOException;

import org.apache.commons.lang3.NotImplementedException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
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

	@GwtIncompatible
	public static class Serializer extends JsonSerializer<TextUnit> {
		public Serializer(JsonSerializer<TextUnit> defaultSerializer) {
            this.defaultSerializer = defaultSerializer;
        }
		@Override
		public void serialize(TextUnit value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
			// since we are using type info, this is not called, but we need it anyway
	        value.getHtml();	// ensure HTML is calculated
	        defaultSerializer.serialize(value, jgen, provider);
		}
		@Override
		public void serializeWithType(TextUnit value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
			value.getHtml();	// ensure HTML is calculated
			defaultSerializer.serializeWithType(value, gen, serializers, typeSer);
		}
		private final JsonSerializer<TextUnit> defaultSerializer;
	}
	
	public static enum MarkupType {
		None,
		Markdown
	}

	@Override
	public Attributes getAttributes() { return (Attributes) super.getAttributes(); }
	@Override
	public void setAttributes(Object attributes) { super.setAttributes((Attributes) attributes); }

	@JsonProperty("html")
	public String getHtmlSnapshot() { return html; }
	public void setHtmlSnapshot(String html) { this.html = html; }	// only for JOSN deserialization purposes

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
