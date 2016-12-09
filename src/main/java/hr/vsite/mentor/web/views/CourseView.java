package hr.vsite.mentor.web.views;

import java.util.List;
import java.util.logging.Logger;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HTML;

import hr.vsite.mentor.course.Course;
import hr.vsite.mentor.lecture.Lecture;
import hr.vsite.mentor.web.Loader;
import hr.vsite.mentor.web.Places;
import hr.vsite.mentor.web.places.CoursePlace;
import hr.vsite.mentor.web.places.LecturePlace;
import hr.vsite.mentor.web.services.Api;
import hr.vsite.mentor.web.widgets.CourseBanner;
import hr.vsite.mentor.web.widgets.LectureWidget;
import hr.vsite.mentor.web.widgets.TocTitle;

import gwt.material.design.client.base.HasTextAlign;
import gwt.material.design.client.constants.HideOn;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.ProgressType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialPushpin;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialScrollspy;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.client.ui.animate.MaterialAnimation;
import gwt.material.design.client.ui.animate.Transition;

public class CourseView extends MaterialPanel {

	private static enum LoadComponent {
		Course,
		Lectures
	};
	
	public static CourseView get() {
		if (instance == null)
			instance = new CourseView();
		return instance;
	}
	
//	public interface Resources extends ApplicationShell.Resources {
//	    @Source(value = { ApplicationShell.Style.DefaultCss, "CourseView.gss" })
//	    CourseView.Style style();
//	}
//
//	@CssResource.ImportedWithPrefix("CourseView")
//	public interface Style extends ApplicationShell.Style {
//		String toc();
//	}
	
	private CourseView() {
		
		MaterialRow row = new MaterialRow();
			MaterialColumn tocColumn = new MaterialColumn();
			tocColumn.setGrid("l2");
			tocColumn.setHideOn(HideOn.HIDE_ON_MED_DOWN);
				toc = new MaterialScrollspy();
				toc.addStyleName(ApplicationShell.res().style().toc());
			tocColumn.add(toc);
				HTML dummy = new HTML("&nbsp;");	// for column to survive pinning of TOC
			tocColumn.add(dummy);
		row.add(tocColumn);
			MaterialColumn lecturesColumn = new MaterialColumn();
			lecturesColumn.setGrid("s12 m12 l8");
				courseBanner = new CourseBanner();
			lecturesColumn.add(courseBanner);
				lecturesContainer = new MaterialPanel();
			lecturesColumn.add(lecturesContainer);
		row.add(lecturesColumn);
		add(row);
		
		MaterialPushpin.apply(toc, 64.0, 0.0);
		
	}
	
	public void show(AcceptsOneWidget containerWidget) {
		containerWidget.setWidget(ApplicationShell.get().wrap(this));
	}

	public void init(CoursePlace place) {

		courseBanner.setVisible(false);
		lecturesContainer.clear();
		toc.clear();
		
		course = null;
		lectures = null;

		// TODO cancel loader if still running

		Loader<LoadComponent> loader = new Loader<LoadComponent>()
			.setCancelOnError(true)
			.setProgress(ApplicationShell.get(), ProgressType.INDETERMINATE);
		loader.add(LoadComponent.Course, Api.get().course().findById(place.getCourseId(), new MethodCallback<Course>() {
			@Override
			public void onSuccess(Method method, Course course) {
				CourseView.this.course = course;
				if (course == null) {
					loader.error(LoadComponent.Course);
					MaterialToast.fireToast("Nije moguće učitati kolegij!");
					return;
				}
				courseBanner.setCourse(course);
				courseBanner.setVisible(true);
				loader.success(LoadComponent.Course);
			}
			@Override
			public void onFailure(Method method, Throwable exception) {
				loader.error(LoadComponent.Course);
				MaterialToast.fireToast("Nije moguće učitati kolegij! " + exception);
			}
		}));
		loader.add(LoadComponent.Lectures, Api.get().lecture().list(null, null, place.getCourseId(), 100, 0, new MethodCallback<List<Lecture>>() {
			@Override
			public void onSuccess(Method method, List<Lecture> lectures) {
				CourseView.this.lectures = lectures;
				if (lectures == null) {
					loader.error(LoadComponent.Lectures);
					MaterialToast.fireToast("Nije moguće učitati lekcije!");
					return;
				}
				for (Lecture lecture : lectures) {
					LectureWidget widget = new LectureWidget(course, lecture);
					//widget.setScrollspy(lecture.getId().toString());	// TODO TOC/Scrollspy does not work
					lecturesContainer.add(widget);
				}
				MaterialAnimation animation = new MaterialAnimation();
		        animation.setTransition(Transition.SHOW_GRID);
		        animation.animate(lecturesContainer);
				loader.success(LoadComponent.Lectures);
			}
			@Override
			public void onFailure(Method method, Throwable exception) {
				loader.error(LoadComponent.Lectures);
				MaterialToast.fireToast("Nije moguće učitati lekcije! " + exception);
			}
		}));
		loader.setLoadedHandler(() -> {
			toc.add(new TocTitle(course.getTitle(), course.getDescription()));
			MaterialIcon courseIcon = new MaterialIcon(IconType.ARROW_DOWNWARD);
			toc.add(courseIcon);
			if (courseIcon.getParent() instanceof HasTextAlign)
				((HasTextAlign) courseIcon.getParent()).setTextAlign(TextAlign.CENTER);	// dirty, but works
			for (Lecture lecture : lectures) {
				MaterialLink tocLink = new MaterialLink(lecture.getTitle(), Places.mapper().getToken(new LecturePlace(course.getId(), lecture.getId())));
				//tocLink.setHref("#" + lecture.getId());	// TODO TOC/Scrollspy does not work
				toc.add(tocLink);
			}
		});

	}

	private final MaterialScrollspy toc;
	private final CourseBanner courseBanner;
	private final MaterialPanel lecturesContainer;

	private Course course = null;
	private List<Lecture> lectures = null;
	
	private static CourseView instance = null;

	@SuppressWarnings("unused")
	private static final Logger Log = Logger.getLogger(CourseView.class.getName());
//	private static final Resources res = GWT.create(Resources.class);
//	static {
//		res.style().ensureInjected();
//	}

}
