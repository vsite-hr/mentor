package hr.vsite.mentor.web.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;

import hr.vsite.mentor.unit.Unit;

import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialLabel;

public class UnknownUnitWidget extends UnitWidget {

	public interface Resources extends UnitWidget.Resources {
		@Source({UnitWidget.Style.DefaultCss, "UnknownUnitWidget.gss"})
		public Style style();
	}

	@CssResource.ImportedWithPrefix("UnknownUnitWidget")
	public interface Style extends UnitWidget.Style {
	}
	
	public UnknownUnitWidget(Unit unit) {

		super();
		
		MaterialColumn messageColumn = new MaterialColumn();
		messageColumn.setGrid("s12");
		messageColumn.setTextAlign(TextAlign.CENTER);
			MaterialLabel message = new MaterialLabel("Unsupported unit type (" + unit.getType() + ")");
			message.addStyleName("flow-text");
		messageColumn.add(message);
		getContentRow().add(messageColumn);
	
		setUnit(unit);
		
	}

	private static final Resources res = GWT.create(Resources.class);
	static {
		res.style().ensureInjected();
	}

}
