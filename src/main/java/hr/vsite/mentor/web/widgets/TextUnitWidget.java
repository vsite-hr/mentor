package hr.vsite.mentor.web.widgets;

import com.google.gwt.core.client.GWT;

import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.HTML;

import hr.vsite.mentor.unit.TextUnit;

import gwt.material.design.client.ui.MaterialColumn;

public class TextUnitWidget extends UnitWidget {

	public interface Resources extends UnitWidget.Resources {
		@Source({UnitWidget.Style.DefaultCss, "TextUnitWidget.gss"})
		public Style style();
	}

	@CssResource.ImportedWithPrefix("TextUnitWidget")
	public interface Style extends UnitWidget.Style {
	}
	
	public TextUnitWidget(TextUnit unit) {

		super();
		
		MaterialColumn textColumn = new MaterialColumn();
		textColumn.setGrid("s12");
			html = new HTML();
			//html.addStyleName("flow-text");
		textColumn.add(html);
		getContentRow().add(textColumn);
	
		if (unit != null)
			setUnit(unit);
		
	}

	public TextUnit getUnit() { return (TextUnit) super.getUnit(); }

	public void setUnit(TextUnit unit) {

		super.setUnit(unit);
		
		html.setHTML(unit.getHtmlSnapshot());
		
	}

	private final HTML html;
	
	private static final Resources res = GWT.create(Resources.class);
	static {
		res.style().ensureInjected();
	}

}
