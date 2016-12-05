package hr.vsite.mentor.web.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ClientBundle.Source;
import com.google.gwt.user.client.ui.Composite;

import hr.vsite.mentor.lecture.Lecture;
import hr.vsite.mentor.web.Places;
import hr.vsite.mentor.web.theme.Theme;
import hr.vsite.mentor.web.widgets.LectureCard.Resources;
import hr.vsite.mentor.web.widgets.LectureCard.Style;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.HideOn;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.themes.client.ThemeLoader;

public class LectureWidget extends Composite {

	public interface Resources extends ClientBundle {
		@Source("LectureWidget.gss")
		public Style style();
	}

	@CssResource.ImportedWithPrefix("LectureWidget")
	public interface Style extends CssResource {
		String view();
		String title();
		String image();
		String description();
	}
	
	public LectureWidget(Lecture lecture) {

		MaterialPanel view = new MaterialPanel();
		view.setBackgroundColor(Color.WHITE);
		view.setShadow(1);
		view.addStyleName(res.style().view());
			MaterialRow titleRow = new MaterialRow();
			titleRow.setBackgroundColor(Color.RED_LIGHTEN_2);
			titleRow.setPadding(5);
				MaterialColumn titleColumn = new MaterialColumn();
				titleColumn.setGrid("s12");
					title = new MaterialLink();
					title.addStyleName(res.style().title());
					title.setTextColor(Color.GREY_LIGHTEN_3);
				titleColumn.add(title);
			titleRow.add(titleColumn);
		view.add(titleRow);
			MaterialRow contentRow = new MaterialRow();
				MaterialColumn imageColumn = new MaterialColumn();
				imageColumn.setGrid("s12 m6 l4");
					image = new MaterialImage();
					image.addStyleName(res.style().image());
					image.addStyleName(Theme.bundle().style().clickable());
				imageColumn.add(image);
			contentRow.add(imageColumn);
				MaterialColumn descriptionColumn = new MaterialColumn();
				descriptionColumn.setGrid("s12 m6 l8");
					description = new MaterialLabel();
					description.addStyleName(res.style().description());
					description.addStyleName("flow-text");
				descriptionColumn.add(description);
			contentRow.add(descriptionColumn);
		view.add(contentRow);
		
		initWidget(view);
		
		setLecture(lecture);
		
		//image.addClickHandler(e -> Places.controller().goTo(new LecturePlace(lecture.getId())));
		
	}

	public void setLecture(Lecture lecture) {

		this.lecture = lecture;
		
		String lectureHref = "#"/* + Places.mapper().getToken(new LecturePlace(lecture.getId()))*/;	// TODO
		
		title.setText(lecture.getTitle());
		title.setHref(lectureHref);
		image.setUrl("https://www.vsite.hr/sites/default/files/promocija2015_027.JPG");	// TODO
		description.setText(lecture.getDescription());
//		unitsLink.setText("13 units");	// TODO
//		unitsLink.setHref(lectureHref);
		
	}

	private final MaterialImage image;
	private final MaterialLink title;
	private final MaterialLabel description;
//	private final MaterialLink unitsLink;
	private Lecture lecture = null;
	
	private static final Resources res = GWT.create(Resources.class);
	static {
		res.style().ensureInjected();
	}

}
