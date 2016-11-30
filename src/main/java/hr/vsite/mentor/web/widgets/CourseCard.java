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

import hr.vsite.mentor.course.Course;
import hr.vsite.mentor.web.Places;
import hr.vsite.mentor.web.places.CoursePlace;

public class CourseCard extends MaterialCard {

	public interface Resources extends ClientBundle {
		@Source("CourseCard.gss")
		public Style style();
	}

	@CssResource.ImportedWithPrefix("CourseCard")
	public interface Style extends CssResource {
		String title();
		String description();
	}
	
	public CourseCard(Course course) {
	
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
		
		image.addClickHandler(e -> Places.controller().goTo(new CoursePlace(this.course.getId())));
		
		setCourse(course);

	}

	public void setCourse(Course course) {

		this.course = course;
		
		String courseHref = "#" + Places.mapper().getToken(new CoursePlace(course.getId()));
		
		image.setUrl("https://www.vsite.hr/sites/default/files/promocija2015_027.JPG");	// TODO
		title.setText(course.getTitle());
		title.setHref(courseHref);
		description.setText(course.getDescription());
		lecturesLink.setText("7 lectures");	// TODO
		lecturesLink.setHref(courseHref);
		
	}

	private final MaterialImage image;
	private final MaterialLink title;
	private final MaterialLabel description;
	private final MaterialLink lecturesLink;
	private Course course = null;
	
	private static final Resources res = GWT.create(Resources.class);
	static {
		res.style().ensureInjected();
	}

}
