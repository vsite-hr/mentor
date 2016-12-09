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

public class LecturersBanner extends MaterialPanel {

	public interface Resources extends ClientBundle {
		@Source("LecturersBanner.gss")
		public Style style();
		@Source("lecturers.jpg")
		public ImageResource recturers();
	}

	@CssResource.ImportedWithPrefix("LecturersBanner")
	public interface Style extends CssResource {
		String view();
		String title();
		String image();
	}

	public LecturersBanner() {

		setBackgroundColor(Color.WHITE);
		addStyleName(res.style().view());
		setShadow(1);

		MaterialRow row = new MaterialRow();
			MaterialColumn textColumn = new MaterialColumn();
			textColumn.setGrid("s12 m6");
				Heading title = new Heading(HeadingSize.H1);
				title.setText("Predavači");
				title.addStyleName(res.style().title());
				title.setTextColor(Color.BLUE_GREY);
			textColumn.add(title);
		row.add(textColumn);
			MaterialColumn imageColumn = new MaterialColumn();
			imageColumn.setGrid("s12 m6");
				MaterialImage banner = new MaterialImage(res.recturers());
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
