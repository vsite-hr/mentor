package hr.vsite.mentor.web.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

import gwt.material.design.client.base.HasTitle;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Heading;
import gwt.material.design.client.ui.html.Paragraph;

public class TocTitle extends Div implements HasTitle {

	public interface Resources extends ClientBundle {
		@Source("TocTitle.gss")
		public Style style();
	}

	@CssResource.ImportedWithPrefix("TocTitle")
	public interface Style extends CssResource {
		String view();
		String link();
		String title();
		String description();
	}
	
	public TocTitle() {
		this(null);
	}

	public TocTitle(String title) {
		this(title, null);
	}

	public TocTitle(String title, String description) {
		this(title, description, null);
	}
	
	public TocTitle(String title, String description, String href) {

		setBackgroundColor(Color.GREY_LIGHTEN_5);
		setShadow(1);
		addStyleName(res.style().view());
		
		link = new MaterialLink();
		link.addStyleName(res.style().link());
		link.setHref(href);
			header = new Heading(HeadingSize.H4);
			header.addStyleName(res.style().title());
		link.add(header);
		add(href != null ? link : header);

		paragraph = new Paragraph();
		paragraph.addStyleName(res.style().description());
		add(paragraph);

		setTitle(title);
		setDescription(description);

	}

	@Override
	public void setTitle(String title) {
		header.setText(title);
	}

	@Override
	public void setDescription(String description) {
		paragraph.setText(description);
	}

	private final MaterialLink link;
	private final Heading header;
	private final Paragraph paragraph;

	private static final Resources res = GWT.create(Resources.class);
	static {
		res.style().ensureInjected();
	}

}