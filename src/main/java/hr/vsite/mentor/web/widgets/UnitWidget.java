package hr.vsite.mentor.web.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

import hr.vsite.mentor.unit.TextUnit;
import hr.vsite.mentor.unit.Unit;
import hr.vsite.mentor.unit.VideoUnit;

import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.constants.IconPosition;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.WavesType;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.animate.MaterialAnimation;
import gwt.material.design.client.ui.animate.Transition;
import gwt.material.design.client.ui.html.Heading;

public abstract class UnitWidget extends MaterialPanel {

	public static UnitWidget create(Unit unit) {
		switch (unit.getType()) {
			case Text: return new TextUnitWidget((TextUnit) unit);
			case Video: return new VideoUnitWidget((VideoUnit) unit);
			case Audio: return new UnknownUnitWidget(unit);
			case Image: return new UnknownUnitWidget(unit);
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
		String next();
		String nextLink();
		String sub();
	}
	
	protected UnitWidget() {

		addStyleName(res.style().view());
		
		titlePanel = new MaterialPanel();
		titlePanel.addStyleName(res.style().titlePanel());
			title = new Heading(HeadingSize.H4);
			title.addStyleName(res.style().title());
		titlePanel.add(title);
		add(titlePanel);
		
		contentRow = new MaterialRow();
		contentRow.addStyleName(res.style().content());
		add(contentRow);

		nextRow = new MaterialRow();
		nextRow.addStyleName(res.style().next());
		nextRow.setVisible(false);
		add(nextRow);

//			MaterialIcon nextIcon = new MaterialIcon(IconType.NAVIGATE_NEXT);
//			nextIcon.setIconPosition(IconPosition.LEFT);
//			nextIcon.setMarginRight(0);
		nextLink = new MaterialLink(IconType.NAVIGATE_NEXT);
		nextLink.setIconPosition(IconPosition.LEFT);
		nextLink.setWaves(WavesType.DEFAULT);
		nextLink.addStyleName(res.style().nextLink());

		nextLink.addClickHandler(e -> onNext());

	}

	public Unit getUnit() { return unit; }

	public void setUnit(Unit unit) {

		this.unit = unit;
		
		title.setText(unit.getTitle());
		
		if (unit.getNextUnit() != null) {
			nextRow.clear();
			nextLink.setText(unit.getNextUnit().getTitle());
			nextLink.setTitle("Idući korak: " + unit.getNextUnit().getTitle());
			nextRow.add(nextLink);
			nextRow.setVisible(true);
		} else {
			nextRow.setVisible(false);
		}
		
	}

	private void onNext() {
		
		nextRow.clear();
		
		UnitWidget subWidget = create(unit.getNextUnit());
		subWidget.addStyleName(res.style().sub());
		nextRow.add(subWidget);

		MaterialAnimation animation = new MaterialAnimation();
        animation.setTransition(Transition.SLIDEINLEFT);
        animation.animate(nextRow);

	}

	protected MaterialPanel getTitlePanel() { return titlePanel; }
	protected MaterialRow getContentRow() { return contentRow; }

	private final MaterialPanel titlePanel;
	private final Heading title;
	private final MaterialRow contentRow;
	private final MaterialRow nextRow;
	private final MaterialLink nextLink;
	private Unit unit = null;
	
	private static final Resources res = GWT.create(Resources.class);
	static {
		res.style().ensureInjected();
	}
	
}
