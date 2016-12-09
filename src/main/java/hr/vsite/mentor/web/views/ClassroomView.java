package hr.vsite.mentor.web.views;

import java.util.List;
import java.util.logging.Logger;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;

import hr.vsite.mentor.course.Course;
import hr.vsite.mentor.user.User;
import hr.vsite.mentor.web.Loader;
import hr.vsite.mentor.web.places.ClassroomPlace;
import hr.vsite.mentor.web.services.Api;
import hr.vsite.mentor.web.widgets.CourseCard;
import hr.vsite.mentor.web.widgets.ClassroomBanner;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.constants.ProgressType;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.client.ui.animate.MaterialAnimation;
import gwt.material.design.client.ui.animate.Transition;
import gwt.material.design.client.ui.html.Heading;

public class ClassroomView extends MaterialPanel {

	private static enum LoadComponent {
		Lecturer,
		Courses
	};

	public static ClassroomView get() {
		if (instance == null)
			instance = new ClassroomView();
		return instance;
	}
	
	public interface Resources extends ApplicationShell.Resources {
	    @Source(value = { ApplicationShell.Style.DefaultCss, "ClassroomView.gss" })
	    ClassroomView.Style style();
	}

	@CssResource.ImportedWithPrefix("ClassroomView")
	public interface Style extends ApplicationShell.Style {
		String filtersPanel();
		String filterKey();
		String filterValue();
	}
	
	private ClassroomView() {
		
		super();

		MaterialRow bannerRow = new MaterialRow();
			MaterialColumn bannerColumn = new MaterialColumn();
			bannerColumn.setGrid("s12 m12 l8");
			bannerColumn.setOffset("l2");
				ClassroomBanner baner = new ClassroomBanner();
			bannerColumn.add(baner);
		bannerRow.add(bannerColumn);
		add(bannerRow);
		
		filtersRow = new MaterialRow();
			MaterialColumn filtersColumn = new MaterialColumn();
			filtersColumn.setGrid("s12 m10");
			filtersColumn.setOffset("s0 m1");
				filtersPanel = new MaterialPanel();
				filtersPanel.addStyleName(res.style().filtersPanel());
			filtersColumn.add(filtersPanel);
		filtersRow.add(filtersColumn);
		filtersRow.setVisible(false);
		add(filtersRow);
		
		MaterialRow coursesRow = new MaterialRow();
			MaterialColumn coursesColumn = new MaterialColumn();
			coursesColumn.setGrid("s12 m10");
			coursesColumn.setOffset("s0 m1");
				coursesContainerRow = new MaterialRow();
			coursesColumn.add(coursesContainerRow);
		coursesRow.add(coursesColumn);
		add(coursesRow);
		
	}
	
	public void show(AcceptsOneWidget containerWidget) {
		containerWidget.setWidget(ApplicationShell.get().wrap(this));
	}

	public void init(ClassroomPlace place) {

		filtersRow.setVisible(false);
		filtersPanel.clear();
		coursesContainerRow.clear();

		lecturer = null;
		courses = null;
		
		// TODO cancel loader if still running

		Loader<LoadComponent> loader = new Loader<LoadComponent>()
			.setCancelOnError(true)
			.setProgress(ApplicationShell.get(), ProgressType.INDETERMINATE);
		if (place.getFilter() != null && place.getFilter().getLecturerId() != null)
			loader.add(LoadComponent.Lecturer, Api.get().user().findById(place.getFilter().getLecturerId(), new MethodCallback<User>() {
				@Override
				public void onSuccess(Method method, User lecturer) {
					ClassroomView.this.lecturer = lecturer;
					if (lecturer == null) {
						loader.error(LoadComponent.Lecturer);
						MaterialToast.fireToast("Nepoznat predavač!");
						return;
					}
					addFilter("Predavač", lecturer.getName());
					filtersRow.setVisible(true);
					loader.success(LoadComponent.Lecturer);
				}
				@Override
				public void onFailure(Method method, Throwable exception) {
					loader.error(LoadComponent.Lecturer);
					MaterialToast.fireToast("Nepoznat predavač! " + exception);
				}
			}));
		loader.add(LoadComponent.Courses, Api.get().course().list(null, place.getFilter() != null ? place.getFilter().getLecturerId() : null, 100, 0, new MethodCallback<List<Course>>() {
			@Override
			public void onSuccess(Method method, List<Course> courses) {
				ClassroomView.this.courses = courses;
				if (courses == null) {
					loader.error(LoadComponent.Courses);
					MaterialToast.fireToast("Nije moguće učitati kolegije!");
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
				loader.success(LoadComponent.Courses);
			}
			@Override
			public void onFailure(Method method, Throwable exception) {
				loader.error(LoadComponent.Courses);
				MaterialToast.fireToast("Nije moguće učitati kolegije! " + exception);
			}
		}));
		
	}

	private void addFilter(String key, String value) {
		Heading keyLabel = new Heading(HeadingSize.H4);
		keyLabel.setText(key + ":");
		keyLabel.addStyleName(res.style().filterKey());
		filtersPanel.add(keyLabel);
		Heading valueLabel = new Heading(HeadingSize.H4);
		valueLabel.setText(value);
		valueLabel.setTextColor(Color.GREY);
		valueLabel.addStyleName(res.style().filterValue());
		filtersPanel.add(valueLabel);
	}
	
	private final MaterialRow filtersRow;
	private final MaterialPanel filtersPanel;
	private final MaterialRow coursesContainerRow;

	@SuppressWarnings("unused")
	private User lecturer = null;
	@SuppressWarnings("unused")
	private List<Course> courses = null;
	
	private static ClassroomView instance = null;
	
	@SuppressWarnings("unused")
	private static final Logger Log = Logger.getLogger(ClassroomView.class.getName());
	private static final Resources res = GWT.create(Resources.class);
	static {
		res.style().ensureInjected();
	}

}
