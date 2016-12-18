package hr.vsite.mentor.web.views;

import java.util.List;
import java.util.logging.Logger;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HTML;

import hr.vsite.mentor.course.Course;
import hr.vsite.mentor.lecture.Lecture;
import hr.vsite.mentor.unit.Unit;
import hr.vsite.mentor.web.Loader;
import hr.vsite.mentor.web.Places;
import hr.vsite.mentor.web.places.CoursePlace;
import hr.vsite.mentor.web.places.LecturePlace;
import hr.vsite.mentor.web.services.Api;
import hr.vsite.mentor.web.widgets.LectureBanner;
import hr.vsite.mentor.web.widgets.TocTitle;
import hr.vsite.mentor.web.widgets.UnitWidget;

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

public class LectureView extends MaterialPanel {

	private static enum LoadComponent {
		Course,
		Lecture,
		Units
	};
	
	public static LectureView get() {
		if (instance == null)
			instance = new LectureView();
		return instance;
	}
	
//	public interface Resources extends ApplicationShell.Resources {
//	    @Source(value = { ApplicationShell.Style.DefaultCss, "LectureView.gss" })
//	    LectureView.Style style();
//	}
//
//	@CssResource.ImportedWithPrefix("LectureView")
//	public interface Style extends ApplicationShell.Style {
//		String view();
//		String news();
//	}
	
	private LectureView() {

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
			MaterialColumn unitsColumn = new MaterialColumn();
			unitsColumn.setGrid("s12 m12 l8");
				lectureBanner = new LectureBanner();
			unitsColumn.add(lectureBanner);
				unitsContainer = new MaterialPanel();
			unitsColumn.add(unitsContainer);
		row.add(unitsColumn);
		add(row);
		
		MaterialPushpin.apply(toc, 64.0, 0.0);
		
	}
	
	public void show(AcceptsOneWidget containerWidget) {
		containerWidget.setWidget(ApplicationShell.get().wrap(this));
	}

	public void init(LecturePlace place) {

		lectureBanner.setVisible(false);
		unitsContainer.clear();
		toc.clear();

		course = null;
		lecture = null;
		units = null;

		// TODO cancel loader if still running
		
		Loader<LoadComponent> loader = new Loader<LoadComponent>()
			.setCancelOnError(true)
			.setProgress(ApplicationShell.get(), ProgressType.INDETERMINATE);
		loader.add(LoadComponent.Course, Api.get().course().findById(place.getCourseId(), new MethodCallback<Course>() {
			@Override
			public void onSuccess(Method method, Course course) {
				LectureView.this.course = course;
				if (course == null) {
					loader.error(LoadComponent.Course);
					MaterialToast.fireToast("Nije moguće učitati kolegij!");
					return;
				}
				loader.success(LoadComponent.Course);
			}
			@Override
			public void onFailure(Method method, Throwable exception) {
				loader.error(LoadComponent.Course);
				MaterialToast.fireToast("Nije moguće učitati kolegij! " + exception);
			}
		}));
		loader.add(LoadComponent.Lecture, Api.get().lecture().findById(place.getLectureId(), new MethodCallback<Lecture>() {
			@Override
			public void onSuccess(Method method, Lecture lecture) {
				LectureView.this.lecture = lecture;
				if (lecture == null) {
					loader.error(LoadComponent.Lecture);
					MaterialToast.fireToast("Nije moguće učitati lekciju!");
					return;
				}
				lectureBanner.setLecture(lecture);
				lectureBanner.setVisible(true);
				loader.success(LoadComponent.Lecture);
			}
			@Override
			public void onFailure(Method method, Throwable exception) {
				loader.error(LoadComponent.Lecture);
				MaterialToast.fireToast("Nije moguće učitati lekciju! " + exception);
			}
		}));
		loader.add(LoadComponent.Units, Api.get().lecture().getUnits(place.getLectureId(), new MethodCallback<List<Unit>>() {
			@Override
			public void onSuccess(Method method, List<Unit> units) {
				LectureView.this.units = units;
				if (units == null) {
					loader.error(LoadComponent.Units);
					MaterialToast.fireToast("Nije moguće učitati nastavne materijale!");
					return;
				}
				for (Unit unit : units) {
					unitsContainer.add(UnitWidget.create(unit));
				}
				MaterialAnimation animation = new MaterialAnimation();
		        animation.setTransition(Transition.SHOW_GRID);
		        animation.animate(unitsContainer);
				loader.success(LoadComponent.Units);
			}
			@Override
			public void onFailure(Method method, Throwable exception) {
				loader.error(LoadComponent.Units);
				MaterialToast.fireToast("Nije moguće učitati nastavne materijale! " + exception);
			}
		}));
		loader.setLoadedHandler(() -> {
			toc.add(new TocTitle(course.getTitle(), course.getDescription(), "#" + Places.mapper().getToken(new CoursePlace(course.getId()))));
			MaterialIcon courseIcon = new MaterialIcon(IconType.ARROW_DOWNWARD);
			toc.add(courseIcon);
			if (courseIcon.getParent() instanceof HasTextAlign)
				((HasTextAlign) courseIcon.getParent()).setTextAlign(TextAlign.CENTER);	// dirty, but works
			toc.add(new TocTitle(lecture.getTitle(), lecture.getDescription()));
			MaterialIcon lectureIcon = new MaterialIcon(IconType.ARROW_DOWNWARD);
			toc.add(lectureIcon);
			if (lectureIcon.getParent() instanceof HasTextAlign)
				((HasTextAlign) lectureIcon.getParent()).setTextAlign(TextAlign.CENTER);	// dirty, but works
			for (Unit unit : units) {
				MaterialLink tocLink = new MaterialLink(unit.getTitle()/*, Places.mapper().getToken(new LecturePlace(course.getId(), lecture.getId()))*/);
				//tocLink.setHref("#" + unit.getId());	// TODO TOC/Scrollspy does not work
				toc.add(tocLink);
			}
		});

	}

	private final MaterialScrollspy toc;
	private final LectureBanner lectureBanner;
	private final MaterialPanel unitsContainer;
	
	private Course course = null;
	private Lecture lecture = null;
	private List<Unit> units = null;
	
	private static LectureView instance = null;
	
	@SuppressWarnings("unused")
	private static final Logger Log = Logger.getLogger(LectureView.class.getName());
//	private static final Resources res = GWT.create(Resources.class);
//	static {
//		res.style().ensureInjected();
//	}

}
