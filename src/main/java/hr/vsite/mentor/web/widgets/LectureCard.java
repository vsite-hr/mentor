package hr.vsite.mentor.web.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.constants.WavesType;
import gwt.material.design.client.ui.MaterialCard;
import gwt.material.design.client.ui.MaterialCardAction;
import gwt.material.design.client.ui.MaterialCardContent;
import gwt.material.design.client.ui.MaterialCardImage;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;

import hr.vsite.mentor.lecture.Lecture;
import hr.vsite.mentor.web.Places;

public class LectureCard extends MaterialCard {

	public interface Resources extends ClientBundle {
		@Source("LectureCard.gss")
		public Style style();
	}

	@CssResource.ImportedWithPrefix("LectureCard")
	public interface Style extends CssResource {
		String title();
		String description();
	}
	
	public LectureCard(Lecture lecture) {
	
		setBackgroundColor(Color.WHITE);
		setHoverable(true);
		
		MaterialCardImage cardImage = new MaterialCardImage();
		cardImage.setWaves(WavesType.LIGHT);
			image = new MaterialImage();
		cardImage.add(image);
		add(cardImage);
		
		MaterialCardContent cardContent = new MaterialCardContent();
			title = new MaterialLink();
			title.addStyleName(res.style().title());
		cardContent.add(title);
			description = new MaterialLabel();
			description.addStyleName(res.style().description());
		cardContent.add(description);
		add(cardContent);
		
		MaterialCardAction cardAction = new MaterialCardAction();
		cardAction.setTextAlign(TextAlign.RIGHT);
			lecturesLink = new MaterialLink();
		cardAction.add(lecturesLink);
		add(cardAction);
		
		//image.addClickHandler(e -> Places.controller().goTo(new LecturePlace(this.lecture.getId())));	// TODO
		
		setLecture(lecture);

	}

	public void setLecture(Lecture lecture) {

		this.lecture = lecture;
		
		String courseHref = "#"/* + Places.mapper().getToken(new LecturePlace(lecture.getId()))*/;	// TODO
		
		image.setUrl("https://www.vsite.hr/sites/default/files/promocija2015_027.JPG");	// TODO
		title.setText(lecture.getTitle());
		title.setHref(courseHref);
		description.setText(lecture.getDescription());
		lecturesLink.setText("13 units");	// TODO
		lecturesLink.setHref(courseHref);
		
	}

	private final MaterialImage image;
	private final MaterialLink title;
	private final MaterialLabel description;
	private final MaterialLink lecturesLink;
	private Lecture lecture = null;
	
	private static final Resources res = GWT.create(Resources.class);
	static {
		res.style().ensureInjected();
	}

}
