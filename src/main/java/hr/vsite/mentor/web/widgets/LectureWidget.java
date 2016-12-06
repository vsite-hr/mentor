package hr.vsite.mentor.web.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.Composite;

import hr.vsite.mentor.lecture.Lecture;
import hr.vsite.mentor.web.Places;
import hr.vsite.mentor.web.places.LecturePlace;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialChip;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;

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
		String keyword();
	}
	
	public LectureWidget(Lecture lecture) {

		MaterialPanel view = new MaterialPanel();
		view.setBackgroundColor(Color.WHITE);
		view.setShadow(1);
		view.setHoverable(true);
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
					imageLink = new MaterialLink();
						image = new MaterialImage();
						image.addStyleName(res.style().image());
					imageLink.add(image);
				imageColumn.add(imageLink);
			contentRow.add(imageColumn);
				MaterialColumn descriptionKeyowordsColumn = new MaterialColumn();
				descriptionKeyowordsColumn.setGrid("s12 m6 l8");
					description = new MaterialLabel();
					description.addStyleName(res.style().description());
					description.addStyleName("flow-text");
				descriptionKeyowordsColumn.add(description);
					keywordsContainer = new MaterialPanel();
					keywordsContainer.setPaddingBottom(5.0);
				descriptionKeyowordsColumn.add(keywordsContainer);
			contentRow.add(descriptionKeyowordsColumn);
		view.add(contentRow);
		
		initWidget(view);
		
		setLecture(lecture);
		
	}

	public void setLecture(Lecture lecture) {

		this.lecture = lecture;
		
		String lectureHref = "#" + Places.mapper().getToken(new LecturePlace(lecture.getId()));
		
		title.setText(lecture.getTitle());
		title.setHref(lectureHref);
		imageLink.setHref(lectureHref);
		image.setUrl("https://www.vsite.hr/sites/default/files/promocija2015_027.JPG");	// TODO
		description.setText(lecture.getDescription());
//		unitsLink.setText("13 units");	// TODO
//		unitsLink.setHref(lectureHref);
		
		keywordsContainer.clear();
		if (lecture.getKeywords() != null)
			for (String keyword : lecture.getKeywords()) {
				MaterialChip chip = new MaterialChip(keyword);
				chip.addStyleName(res.style().keyword());
				keywordsContainer.add(chip);
			}
		
	}

	private final MaterialLink title;
	private final MaterialLink imageLink;
	private final MaterialImage image;
	private final MaterialLabel description;
	private final MaterialPanel keywordsContainer;
//	private final MaterialLink unitsLink;
	private Lecture lecture = null;
	
	private static final Resources res = GWT.create(Resources.class);
	static {
		res.style().ensureInjected();
	}

}
