package hr.vsite.mentor.web.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.html.Heading;

public class ClassroomBanner extends MaterialPanel {

	public interface Resources extends ClientBundle {
		@Source("ClassroomBanner.gss")
		public Style style();
		@Source("classroom.jpg")
		public ImageResource elearning();
	}

	@CssResource.ImportedWithPrefix("ClassroomBanner")
	public interface Style extends CssResource {
		String view();
		String title();
		String image();
	}

	public ClassroomBanner() {

		setBackgroundColor(Color.WHITE);
		addStyleName(res.style().view());
		setShadow(1);

		MaterialRow row = new MaterialRow();
			MaterialColumn textColumn = new MaterialColumn();
			textColumn.setGrid("s12 m6");
				Heading title = new Heading(HeadingSize.H1);
				title.setText("Vsite digitalna učionica");
				title.addStyleName(res.style().title());
				title.setTextColor(Color.BLUE_GREY);
			textColumn.add(title);
		row.add(textColumn);
			MaterialColumn imageColumn = new MaterialColumn();
			imageColumn.setGrid("s12 m6");
				MaterialImage banner = new MaterialImage(res.elearning());
				banner.addStyleName(res.style().image());
			imageColumn.add(banner);
		row.add(imageColumn);
		add(row);
		
	}

	private static final Resources res = GWT.create(Resources.class);
	static {
		res.style().ensureInjected();
	}

}
