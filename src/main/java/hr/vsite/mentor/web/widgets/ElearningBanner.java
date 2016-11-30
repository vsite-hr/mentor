package hr.vsite.mentor.web.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Composite;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialRow;

public class ElearningBanner extends Composite {

	public interface Resources extends ClientBundle {
		@Source("ElearningBanner.gss")
		public Style style();
		@Source("elearning.jpg")
		public ImageResource elearning();
	}

	@CssResource.ImportedWithPrefix("ElearningBanner")
	public interface Style extends CssResource {
		String view();
		String title();
		String image();
	}

	public ElearningBanner() {

		MaterialRow view = new MaterialRow();
		view.addStyleName(res.style().view());
		view.setShadow(1);
//		view.addStyleName("valign-wrapper");	// breaks grid
			MaterialColumn textColumn = new MaterialColumn();
			textColumn.setGrid("s12 m6");
				MaterialLabel title = new MaterialLabel("Vsite digitalna učionica");
				title.addStyleName(res.style().title());
				title.setTextColor(Color.BLUE_GREY);
			textColumn.add(title);
		view.add(textColumn);
			MaterialColumn imageColumn = new MaterialColumn();
			imageColumn.setGrid("s12 m6");
				MaterialImage banner = new MaterialImage(res.elearning());
				banner.addStyleName(res.style().image());
			imageColumn.add(banner);
		view.add(imageColumn);
		
		initWidget(view);

	}

	private static final Resources res = GWT.create(Resources.class);
	static {
		res.style().ensureInjected();
	}

}
