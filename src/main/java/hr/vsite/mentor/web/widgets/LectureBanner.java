package hr.vsite.mentor.web.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.html.Heading;

import hr.vsite.mentor.lecture.Lecture;

public class LectureBanner extends MaterialPanel {

	public interface Resources extends ClientBundle {
		@Source("LectureBanner.gss")
		public Style style();
	}

	@CssResource.ImportedWithPrefix("LectureBanner")
	public interface Style extends CssResource {
		String view();
		String title();
	}
	
	public LectureBanner() {
		this(null);
	}
	
	public LectureBanner(Lecture lecture) {
	
		setBackgroundColor(Color.WHITE);
		setShadow(1);
		addStyleName(res.style().view());
		
		MaterialRow titleRow = new MaterialRow();
			MaterialColumn titleColumn = new MaterialColumn();
			titleColumn.setGrid("s12");
				title = new Heading(HeadingSize.H1);
				title.addStyleName(res.style().title());
				title.setTextColor(Color.BLUE_GREY);
			titleColumn.add(title);
		titleRow.add(titleColumn);
		add(titleRow);
		
		MaterialRow descriptionRow = new MaterialRow();
			MaterialColumn descriptionColumn = new MaterialColumn();
			descriptionColumn.setGrid("s12");
				description = new MaterialLabel();
				description.setTextColor(Color.GREY_DARKEN_1);
				description.addStyleName("flow-text");
			descriptionColumn.add(description);
		descriptionRow.add(descriptionColumn);
		add(descriptionRow);

		if (lecture != null)
			setLecture(lecture);

	}

	public Lecture getLecture() { return lecture; }
	public void setLecture(Lecture lecture) {
		this.lecture = lecture;
		title.setText(lecture.getTitle());
		description.setText(lecture.getDescription());
	}

	private final Heading title;
	private final MaterialLabel description;
	private Lecture lecture = null;
	
	private static final Resources res = GWT.create(Resources.class);
	static {
		res.style().ensureInjected();
	}

}
