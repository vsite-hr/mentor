package hr.vsite.mentor.web.views;

import java.util.List;
import java.util.UUID;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;

import hr.vsite.mentor.lecture.Lecture;
import hr.vsite.mentor.unit.Unit;
import hr.vsite.mentor.web.Loader;
import hr.vsite.mentor.web.services.Api;
import hr.vsite.mentor.web.widgets.LectureBanner;
import hr.vsite.mentor.web.widgets.UnitWidget;

import gwt.material.design.client.constants.ProgressType;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.client.ui.animate.MaterialAnimation;
import gwt.material.design.client.ui.animate.Transition;

public class LectureView extends Composite {

	private static enum LoadComponent {
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
		
		super();

		MaterialPanel view = new MaterialPanel();
			MaterialRow bannerRow = new MaterialRow();
				MaterialColumn bannerColumn = new MaterialColumn();
				bannerColumn.setGrid("s12 m12 l8");
				bannerColumn.setOffset("l2");
					lectureBanner = new LectureBanner();
				bannerColumn.add(lectureBanner);
			bannerRow.add(bannerColumn);
		view.add(bannerRow);
			MaterialRow unitsRow = new MaterialRow();
				MaterialColumn unitsColumn = new MaterialColumn();
				unitsColumn.setGrid("s12 m12 l8");
				unitsColumn.setOffset("l2");
					unitsContainer = new MaterialPanel();
				unitsColumn.add(unitsContainer);
			unitsRow.add(unitsColumn);
		view.add(unitsRow);
		
		initWidget(view);

	}
	
	public void show(AcceptsOneWidget containerWidget) {
		containerWidget.setWidget(ApplicationShell.get().wrap(this));
	}

	public void init(UUID lectureId) {

		lectureBanner.setVisible(false);
		unitsContainer.clear();

		Loader<LoadComponent> loader = Loader.start(LoadComponent.class)
			.setCancelOnError(true)
			.setProgress(ApplicationShell.get(), ProgressType.INDETERMINATE);
		loader.add(LoadComponent.Lecture, Api.get().lecture().findById(lectureId, new MethodCallback<Lecture>() {
			@Override
			public void onSuccess(Method method, Lecture lecture) {
				if (lecture == null) {
					loader.error(LoadComponent.Lecture);
					MaterialToast.fireToast("Couldn't load lecture!");
					return;
				}
				lectureBanner.setLecture(lecture);
				lectureBanner.setVisible(true);
				loader.success(LoadComponent.Lecture);
			}
			@Override
			public void onFailure(Method method, Throwable exception) {
				loader.error(LoadComponent.Lecture);
				MaterialToast.fireToast("Couldn't load lecture! " + exception);
			}
		}));
		loader.add(LoadComponent.Units, Api.get().unit().list(null, null, null, lectureId, 100, 0, new MethodCallback<List<Unit>>() {
			@Override
			public void onSuccess(Method method, List<Unit> units) {
				if (units == null) {
					loader.error(LoadComponent.Units);
					MaterialToast.fireToast("Couldn't load units!");
					return;
				}
				for (Unit unit : units)
					unitsContainer.add(UnitWidget.create(unit));
				MaterialAnimation animation = new MaterialAnimation();
		        animation.setTransition(Transition.SHOW_GRID);
		        animation.animate(unitsContainer);
				loader.success(LoadComponent.Units);
			}
			@Override
			public void onFailure(Method method, Throwable exception) {
				loader.error(LoadComponent.Units);
				MaterialToast.fireToast("Couldn't load units! " + exception);
			}
		}));
		
	}

	private final LectureBanner lectureBanner;
	private final MaterialPanel unitsContainer;
	
	private static LectureView instance = null;
	
//	private static final Resources res = GWT.create(Resources.class);
//	static {
//		res.style().ensureInjected();
//	}

}
