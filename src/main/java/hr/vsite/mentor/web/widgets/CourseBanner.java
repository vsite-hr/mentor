package hr.vsite.mentor.web.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.Composite;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.HideOn;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.constants.WavesType;
import gwt.material.design.client.ui.MaterialCard;
import gwt.material.design.client.ui.MaterialCardAction;
import gwt.material.design.client.ui.MaterialCardContent;
import gwt.material.design.client.ui.MaterialCardImage;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;

import hr.vsite.mentor.course.Course;

public class CourseBanner extends Composite {

	public interface Resources extends ClientBundle {
		@Source("CourseBanner.gss")
		public Style style();
	}

	@CssResource.ImportedWithPrefix("CourseBanner")
	public interface Style extends CssResource {
		String view();
		String title();
		String image();
		String description();
	}
	
	public CourseBanner() {
		this(null);
	}
	
	public CourseBanner(Course course) {
	
		MaterialPanel view = new MaterialPanel();
		view.setBackgroundColor(Color.WHITE);
		view.setShadow(1);
		view.addStyleName(res.style().view());
			MaterialRow titleRow = new MaterialRow();
				MaterialColumn titleColumn = new MaterialColumn();
				titleColumn.setGrid("s12 m8");
					title = new MaterialLabel();
					title.addStyleName(res.style().title());
					title.setTextColor(Color.BLUE_GREY);
				titleColumn.add(title);
			titleRow.add(titleColumn);
				MaterialColumn lecturerImageColumn = new MaterialColumn();
				lecturerImageColumn.setGrid("m4");
				lecturerImageColumn.setHideOn(HideOn.HIDE_ON_SMALL_DOWN);
					lecturerImage = new MaterialImage();
					lecturerImage.addStyleName(res.style().image());
					lecturerImage.setCircle(true);
				lecturerImageColumn.add(lecturerImage);
			titleRow.add(lecturerImageColumn);
		view.add(titleRow);
			MaterialRow lecturerRow = new MaterialRow();
				MaterialColumn lecturerCaptionColumn = new MaterialColumn();
				lecturerCaptionColumn.setGrid("s6 m4 l3");
					MaterialLabel lecturerCaption = new MaterialLabel("Predavač:");
					lecturerCaption.setTextColor(Color.GREY_LIGHTEN_1);
					lecturerCaption.setTextAlign(TextAlign.RIGHT);
					lecturerCaption.addStyleName("flow-text");
				lecturerCaptionColumn.add(lecturerCaption);
			lecturerRow.add(lecturerCaptionColumn);
				MaterialColumn lecturerNameColumn = new MaterialColumn();
				lecturerNameColumn.setGrid("s6 m8 l9");
					lecturerName = new MaterialLabel();
					lecturerName.setTextColor(Color.GREY_DARKEN_1);
					lecturerName.setTextAlign(TextAlign.LEFT);
					lecturerName.addStyleName("flow-text");
				lecturerNameColumn.add(lecturerName);
			lecturerRow.add(lecturerNameColumn);
		view.add(lecturerRow);
			MaterialRow descriptionRow = new MaterialRow();
				MaterialColumn descriptionCaptionColumn = new MaterialColumn();
				descriptionCaptionColumn.setGrid("s6 m4 l3");
					MaterialLabel descriptionCaption = new MaterialLabel("Opis:");
					descriptionCaption.setTextColor(Color.GREY_LIGHTEN_1);
					descriptionCaption.setTextAlign(TextAlign.RIGHT);
					descriptionCaption.addStyleName("flow-text");
				descriptionCaptionColumn.add(descriptionCaption);
			descriptionRow.add(descriptionCaptionColumn);
				MaterialColumn descriptionColumn = new MaterialColumn();
				descriptionColumn.setGrid("s6 m8 l9");
					description = new MaterialLabel();
					description.setTextColor(Color.GREY_DARKEN_1);
					description.setTextAlign(TextAlign.LEFT);
					description.addStyleName("flow-text");
				descriptionColumn.add(description);
			descriptionRow.add(descriptionColumn);
		view.add(descriptionRow);

		initWidget(view);
		
//		MaterialCardImage cardImage = new MaterialCardImage();
//		cardImage.setWaves(WavesType.LIGHT);
//			image = new MaterialImage();
//		cardImage.add(image);
//		add(cardImage);
//		
//		MaterialCardContent cardContent = new MaterialCardContent();
//		cardContent.add(title);
//			description = new MaterialLabel();
//			description.addStyleName(res.style().description());
//		cardContent.add(description);
//		add(cardContent);
		
//		MaterialCardAction cardAction = new MaterialCardAction();
//		cardAction.setTextAlign(TextAlign.RIGHT);
//			lecturesLink = new MaterialLink();
//		cardAction.add(lecturesLink);
//		add(cardAction);
		
		if (course != null)
			setCourse(course);

	}

	public Course getCourse() { return course; }
	public void setCourse(Course course) {
		this.course = course;
		title.setText(course.getTitle());
		lecturerImage.setUrl(GWT.getHostPageBaseURL() + "api/users/" + course.getAuthor().getId() + "/photo");
		lecturerName.setText(course.getAuthor().getName());
		description.setText(course.getDescription());
	}

	private final MaterialImage lecturerImage;
	private final MaterialLabel title;
	private final MaterialLabel lecturerName;
	private final MaterialLabel description;
	private Course course = null;
	
	private static final Resources res = GWT.create(Resources.class);
	static {
		res.style().ensureInjected();
	}

}
