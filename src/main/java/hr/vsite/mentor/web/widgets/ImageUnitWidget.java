package hr.vsite.mentor.web.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.HTML;

import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialImage;

import hr.vsite.mentor.unit.ImageUnit;

public class ImageUnitWidget extends UnitWidget {

	public interface Resources extends UnitWidget.Resources {
		@Source({UnitWidget.Style.DefaultCss, "ImageUnitWidget.gss"})
		public Style style();
	}

	@CssResource.ImportedWithPrefix("ImageUnitWidget")
	public interface Style extends UnitWidget.Style {
		String caption();
	}
	
	public ImageUnitWidget(ImageUnit unit) {

		super();
		
		MaterialColumn imageColumn = new MaterialColumn();
		imageColumn.setGrid("s12");
		imageColumn.setTextAlign(TextAlign.CENTER);
			image = new MaterialImage();
		imageColumn.add(image);
		getContentRow().add(imageColumn);

		captionColumn = new MaterialColumn();
		captionColumn.setGrid("s12");
		captionColumn.setTextAlign(TextAlign.CENTER);
			caption = new HTML();
			caption.addStyleName(res.style().caption());
		captionColumn.add(caption);
		getContentRow().add(captionColumn);

		if (unit != null)
			setUnit(unit);
		
	}

	public ImageUnit getUnit() { return (ImageUnit) super.getUnit(); }

	public void setUnit(ImageUnit unit) {

		super.setUnit(unit);
		
		image.setUrl(GWT.getHostPageBaseURL() + "api/units/" + unit.getId() + "/content");

		if (unit.getAttributes().getCaption() != null) {
			caption.setHTML(new SafeHtmlBuilder().appendEscapedLines(unit.getAttributes().getCaption()).toSafeHtml());
			captionColumn.setVisible(true);
		} else {
			captionColumn.setVisible(false);
		}
		
	}

	private final MaterialImage image;
	private final MaterialColumn captionColumn;
	private final HTML caption;

	private static final Resources res = GWT.create(Resources.class);
	static {
		res.style().ensureInjected();
	}

}
