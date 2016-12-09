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

import hr.vsite.mentor.user.User;
import hr.vsite.mentor.web.Places;
import hr.vsite.mentor.web.places.ClassroomPlace;

public class LecturerCard extends MaterialCard {

	public interface Resources extends ClientBundle {
		@Source("LecturerCard.gss")
		public Style style();
	}

	@CssResource.ImportedWithPrefix("LecturerCard")
	public interface Style extends CssResource {
		String name();
		//String description();
	}
	
	public LecturerCard(User lecturer) {
	
		setBackgroundColor(Color.WHITE);
		setHoverable(true);
		
		MaterialCardImage cardImage = new MaterialCardImage();
		cardImage.setWaves(WavesType.LIGHT);
			image = new MaterialImage();
		cardImage.add(image);
		add(cardImage);
		
		MaterialCardContent cardContent = new MaterialCardContent();
			name = new MaterialLabel();
			name.addStyleName(res.style().name());
		cardContent.add(name);
//			description = new MaterialLabel();
//			description.addStyleName(res.style().description());
//		cardContent.add(description);
		add(cardContent);
		
		MaterialCardAction cardAction = new MaterialCardAction();
		cardAction.setTextAlign(TextAlign.RIGHT);
			coursesLink = new MaterialLink();
		cardAction.add(coursesLink);
		add(cardAction);
		
		setLecturer(lecturer);

	}

	public User getLecturer() { return lecturer; }

	public void setLecturer(User lecturer) {

		this.lecturer = lecturer;
		
		String classroomHref = "#" + Places.mapper().getToken(new ClassroomPlace(new ClassroomPlace.Filter().setLecturerId(lecturer.getId())));

		image.setUrl(GWT.getHostPageBaseURL() + "api/users/" + lecturer.getId() + "/photo");
		name.setText(lecturer.getName());
		//name.setHref(courseHref);
		//description.setText(course.getDescription());
		coursesLink.setText("7 kolegija");	// TODO
		coursesLink.setHref(classroomHref);
		
	}

	private final MaterialImage image;
	private final MaterialLabel name;
//	private final MaterialLabel description;
	private final MaterialLink coursesLink;
	private User lecturer = null;
	
	private static final Resources res = GWT.create(Resources.class);
	static {
		res.style().ensureInjected();
	}
	
}
