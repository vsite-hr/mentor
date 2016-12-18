package hr.vsite.mentor.web.widgets;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

import hr.vsite.mentor.course.Course;
import hr.vsite.mentor.lecture.Lecture;
import hr.vsite.mentor.web.Places;
import hr.vsite.mentor.web.places.LecturePlace;
import hr.vsite.mentor.web.services.Api;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.MaterialChip;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.html.Heading;
import gwt.material.design.client.ui.html.Paragraph;

public class LectureWidget extends MaterialPanel {

	public interface Resources extends ClientBundle {
		@Source("LectureWidget.gss")
		public Style style();
	}

	@CssResource.ImportedWithPrefix("LectureWidget")
	public interface Style extends CssResource {
		String view();
		String title();
		String titlePanel();
		String image();
		String description();
		String keyword();
		String actionsPanel();
		String unitsLink();
	}
	
	public LectureWidget(Course course, Lecture lecture) {

		setBackgroundColor(Color.WHITE);
		setShadow(1);
		setHoverable(true);
		addStyleName(res.style().view());
			MaterialPanel titlePanel = new MaterialPanel();
			titlePanel.setBackgroundColor(Color.RED_LIGHTEN_2);
			titlePanel.addStyleName(res.style().titlePanel());
				titleLink = new MaterialLink();
					title = new Heading(HeadingSize.H5);
					title.addStyleName(res.style().title());
					title.setTextColor(Color.GREY_LIGHTEN_3);
				titleLink.add(title);
			titlePanel.add(titleLink);
		add(titlePanel);
			MaterialRow contentRow = new MaterialRow();
				MaterialColumn imageColumn = new MaterialColumn();
				imageColumn.setGrid("s12 m6 l4");
					imageLink = new MaterialLink();
						image = new MaterialImage();
						image.addStyleName(res.style().image());
					imageLink.add(image);
				imageColumn.add(imageLink);
			contentRow.add(imageColumn);
				MaterialColumn contentColumn = new MaterialColumn();
				contentColumn.setGrid("s12 m6 l8");
					description = new Paragraph();
					description.addStyleName(res.style().description());
					description.addStyleName("flow-text");
				contentColumn.add(description);
					keywordsContainer = new MaterialPanel();
					keywordsContainer.setPaddingLeft(20.0);
					keywordsContainer.setPaddingRight(20.0);
					keywordsContainer.setPaddingBottom(20.0);
				contentColumn.add(keywordsContainer);
			contentRow.add(contentColumn);
		add(contentRow);
			MaterialPanel actionsPanel = new MaterialPanel();
			actionsPanel.addStyleName(res.style().actionsPanel());
				unitsLink = new MaterialLink();
				unitsLink.addStyleName(res.style().unitsLink());
			actionsPanel.add(unitsLink);
		add(actionsPanel);
		
		setLecture(course, lecture);
		
	}

	public Course getCourse() { return course; }
	public Lecture getLecture() { return lecture; }
	
	public void setLecture(Course course, Lecture lecture) {

		this.course = course;
		this.lecture = lecture;
		
		String lectureHref = "#" + Places.mapper().getToken(new LecturePlace(course.getId(), lecture.getId()));
		
		titleLink.setHref(lectureHref);
		title.setText(lecture.getTitle());
		imageLink.setHref(lectureHref);
		image.setUrl(GWT.getHostPageBaseURL() + "api/lectures/" + lecture.getId() + "/thumbnail");
		description.setText(lecture.getDescription());
		unitsLink.setText("...");
		unitsLink.setTitle("Učitavam broj poglavlja");
		unitsLink.setHref(lectureHref);
		
		keywordsContainer.clear();
		if (lecture.getKeywords() != null)
			for (String keyword : lecture.getKeywords()) {
				MaterialChip chip = new MaterialChip(keyword);
				chip.addStyleName(res.style().keyword());
				keywordsContainer.add(chip);
			}

		Api.get().lecture().getUnitsCount(lecture.getId(), new MethodCallback<Integer>() {
			@Override
			public void onSuccess(Method method, Integer count) {
				unitsLink.setText(count + " poglavlja");
				unitsLink.setTitle("");
			}
			@Override
			public void onFailure(Method method, Throwable exception) {
				unitsLink.setText("<?!?>");
				unitsLink.setTitle("Pogreška prilikom učitavanja broja poglavlja");
			}
		});
		
	}

	private final MaterialLink titleLink;
	private final Heading title;
	private final MaterialLink imageLink;
	private final MaterialImage image;
	private final Paragraph description;
	private final MaterialPanel keywordsContainer;
	private final MaterialLink unitsLink;
	private Course course = null;
	private Lecture lecture = null;
	
	private static final Resources res = GWT.create(Resources.class);
	static {
		res.style().ensureInjected();
	}
	
}
