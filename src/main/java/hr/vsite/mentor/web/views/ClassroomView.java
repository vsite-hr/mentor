package hr.vsite.mentor.web.views;

import java.util.List;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;

import hr.vsite.mentor.course.Course;
import hr.vsite.mentor.web.places.ClassroomPlace;
import hr.vsite.mentor.web.services.Api;
import hr.vsite.mentor.web.widgets.CourseCard;
import hr.vsite.mentor.web.widgets.ClassroomBanner;

import gwt.material.design.client.constants.ProgressType;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.client.ui.animate.MaterialAnimation;
import gwt.material.design.client.ui.animate.Transition;

public class ClassroomView extends Composite {

	public static ClassroomView get() {
		if (instance == null)
			instance = new ClassroomView();
		return instance;
	}
	
//	public interface Resources extends ApplicationShell.Resources {
//	    @Source(value = { ApplicationShell.Style.DefaultCss, "WelcomeView.gss" })
//	    WelcomeView.Style style();
//	}
//
//	@CssResource.ImportedWithPrefix("WelcomeView")
//	public interface Style extends ApplicationShell.Style {
//		String view();
//		String news();
//	}
	
	private ClassroomView() {
		
		super();

		MaterialPanel view = new MaterialPanel();
			MaterialRow bannerRow = new MaterialRow();
				MaterialColumn bannerColumn = new MaterialColumn();
				bannerColumn.setGrid("s12 m12 l8");
				bannerColumn.setOffset("l2");
					ClassroomBanner elearningCard = new ClassroomBanner();
				bannerColumn.add(elearningCard);
			bannerRow.add(bannerColumn);
		view.add(bannerRow);
			MaterialRow coursesRow = new MaterialRow();
				MaterialColumn coursesColumn = new MaterialColumn();
				coursesColumn.setGrid("s12 m10");
				coursesColumn.setOffset("s0 m1");
					coursesContainerRow = new MaterialRow();
				coursesColumn.add(coursesContainerRow);
			coursesRow.add(coursesColumn);
		view.add(coursesRow);
		
		initWidget(view);

	}
	
	public void show(AcceptsOneWidget containerWidget) {
		containerWidget.setWidget(ApplicationShell.get().wrap(this));
	}

	public void init(ClassroomPlace place) {

		coursesContainerRow.clear();

		ApplicationShell.get().showProgress(ProgressType.INDETERMINATE);

		// TODO add loader and cancel it if still running

		Api.get().course().list(null, null, 100, 0, new MethodCallback<List<Course>>() {
			@Override
			public void onSuccess(Method method, List<Course> courses) {
				ApplicationShell.get().hideProgress();
				if (courses == null) {
					MaterialToast.fireToast("Fakap!");
					return;
				}
				for (Course course : courses) {
					MaterialColumn column = new MaterialColumn();
					column.setGrid("s12 m6 l3");
						CourseCard card = new CourseCard(course);
					column.add(card);
					coursesContainerRow.add(column);
				}
				MaterialAnimation animation = new MaterialAnimation();
		        animation.setTransition(Transition.SHOW_GRID);
		        animation.animate(coursesContainerRow);
			}
			@Override
			public void onFailure(Method method, Throwable exception) {
				ApplicationShell.get().hideProgress();
				MaterialToast.fireToast("Fakap! " + exception);
			}
		});
		
	}

	private final MaterialRow coursesContainerRow;
	
	private static ClassroomView instance = null;
	
//	private static final Resources res = GWT.create(Resources.class);
//	static {
//		res.style().ensureInjected();
//	}

}
