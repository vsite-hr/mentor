package hr.vsite.mentor.web.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.Composite;

import hr.vsite.mentor.unit.TextUnit;
import hr.vsite.mentor.unit.Unit;
import hr.vsite.mentor.unit.VideoUnit;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialBadge;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;

public abstract class UnitWidget extends Composite {

	public static UnitWidget create(Unit unit) {
		switch (unit.getType()) {
			case Text: return new TextUnitWidget((TextUnit) unit);
			case Video: return new VideoUnitWidget((VideoUnit) unit);
			case Audio: return new UnknownUnitWidget(unit);
			case Image: return new UnknownUnitWidget(unit);
			case Quiz: return new UnknownUnitWidget(unit);
			case Series: return new UnknownUnitWidget(unit);
			case YouTube: return new UnknownUnitWidget(unit);
		}
		return new UnknownUnitWidget(unit);
	}
	
	public interface Resources extends ClientBundle {
		@Source(Style.DefaultCss)
		public Style style();
	}

	@CssResource.ImportedWithPrefix("UnitWidget")
	public interface Style extends CssResource {
	    String DefaultCss = "hr/vsite/mentor/web/widgets/UnitWidget.gss";
		String view();
		String titlePanel();
		String title();
		String badge();
		String content();
	}
	
	protected UnitWidget() {

		MaterialPanel view = new MaterialPanel();
		view.setBackgroundColor(Color.WHITE);
		view.setShadow(1);
		view.setHoverable(true);
		view.addStyleName(res.style().view());
			titlePanel = new MaterialPanel();
			titlePanel.setBackgroundColor(Color.RED_LIGHTEN_2);
			titlePanel.addStyleName(res.style().titlePanel());
				title = new MaterialLabel();
				title.addStyleName(res.style().title());
				title.setTextColor(Color.GREY_LIGHTEN_3);
			titlePanel.add(title);
		view.add(titlePanel);
//			MaterialRow titleRow = new MaterialRow();
//			titleRow.setBackgroundColor(Color.RED_LIGHTEN_2);
//			titleRow.setPadding(5);
//				MaterialColumn titleColumn = new MaterialColumn();
//				titleColumn.setGrid("s12");
//					title = new MaterialLabel();
//					title.addStyleName(res.style().title());
//					title.setTextColor(Color.GREY_LIGHTEN_3);
//				titleColumn.add(title);
//			titleRow.add(titleColumn);
//		view.add(titleRow);
			contentRow = new MaterialRow();
			contentRow.addStyleName(res.style().content());
		view.add(contentRow);

		initWidget(view);
		
	}

	public Unit getUnit() { return unit; }

	public void setUnit(Unit unit) {

		this.unit = unit;
		
		title.setText(unit.getTitle());
		
	}

	protected MaterialPanel getTitlePanel() { return titlePanel; }
	protected MaterialRow getContentRow() { return contentRow; }

	private final MaterialPanel titlePanel;
	private final MaterialLabel title;
	private final MaterialRow contentRow;
	private Unit unit = null;
	
	private static final Resources res = GWT.create(Resources.class);
	static {
		res.style().ensureInjected();
	}
	
}
