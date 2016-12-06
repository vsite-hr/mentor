package hr.vsite.mentor.web.views;

import java.util.List;
import java.util.UUID;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;

import hr.vsite.mentor.course.Course;
import hr.vsite.mentor.lecture.Lecture;
import hr.vsite.mentor.web.Loader;
import hr.vsite.mentor.web.services.Api;
import hr.vsite.mentor.web.widgets.CourseBanner;
import hr.vsite.mentor.web.widgets.LectureWidget;

import gwt.material.design.client.constants.ProgressType;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.client.ui.animate.MaterialAnimation;
import gwt.material.design.client.ui.animate.Transition;

public class CourseView extends Composite {

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
//		String view();
//		String news();
//	}
	
	private CourseView() {
		
		super();

		MaterialPanel view = new MaterialPanel();
			MaterialRow bannerRow = new MaterialRow();
				MaterialColumn bannerColumn = new MaterialColumn();
				bannerColumn.setGrid("s12 m12 l8");
				bannerColumn.setOffset("l2");
					courseBanner = new CourseBanner();
				bannerColumn.add(courseBanner);
			bannerRow.add(bannerColumn);
		view.add(bannerRow);
			MaterialRow lecturesRow = new MaterialRow();
				MaterialColumn lecturesColumn = new MaterialColumn();
				lecturesColumn.setGrid("s12 m12 l8");
				lecturesColumn.setOffset("l2");
					lecturesContainer = new MaterialPanel();
				lecturesColumn.add(lecturesContainer);
			lecturesRow.add(lecturesColumn);
		view.add(lecturesRow);
		
		initWidget(view);

	}
	
	public void show(AcceptsOneWidget containerWidget) {
		containerWidget.setWidget(ApplicationShell.get().wrap(this));
	}

	public void init(UUID courseId) {

		courseBanner.setVisible(false);
		lecturesContainer.clear();

		Loader<LoadComponent> loader = Loader.start(LoadComponent.class)
			.setCancelOnError(true)
			.setProgress(ApplicationShell.get(), ProgressType.INDETERMINATE);
		loader.add(LoadComponent.Course, Api.get().course().findById(courseId, new MethodCallback<Course>() {
			@Override
			public void onSuccess(Method method, Course course) {
				if (course == null) {
					loader.error(LoadComponent.Course);
					MaterialToast.fireToast("Couldn't load course!");
					return;
				}
				courseBanner.setCourse(course);
				courseBanner.setVisible(true);
				loader.success(LoadComponent.Course);
			}
			@Override
			public void onFailure(Method method, Throwable exception) {
				loader.error(LoadComponent.Course);
				MaterialToast.fireToast("Couldn't load course! " + exception);
			}
		}));
		loader.add(LoadComponent.Lectures, Api.get().lecture().list(null, null, courseId, 100, 0, new MethodCallback<List<Lecture>>() {
			@Override
			public void onSuccess(Method method, List<Lecture> lectures) {
				if (lectures == null) {
					loader.error(LoadComponent.Lectures);
					MaterialToast.fireToast("Couldn't load lectures!");
					return;
				}
				for (Lecture lecture : lectures)
					lecturesContainer.add(new LectureWidget(lecture));
				MaterialAnimation animation = new MaterialAnimation();
		        animation.setTransition(Transition.SHOW_GRID);
		        animation.animate(lecturesContainer);
				loader.success(LoadComponent.Lectures);
			}
			@Override
			public void onFailure(Method method, Throwable exception) {
				loader.error(LoadComponent.Lectures);
				MaterialToast.fireToast("Couldn't load lectures! " + exception);
			}
		}));
		
	}

	private final CourseBanner courseBanner;
	private final MaterialPanel lecturesContainer;
	
	private static CourseView instance = null;
	
//	private static final Resources res = GWT.create(Resources.class);
//	static {
//		res.style().ensureInjected();
//	}

}
