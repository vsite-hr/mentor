package hr.vsite.mentor.web.views;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.http.client.Request;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.IsWidget;

import hr.vsite.mentor.web.Places;

import hr.vsite.mentor.course.Course;
import hr.vsite.mentor.web.MentorBus;
import hr.vsite.mentor.web.places.ClassroomPlace;
import hr.vsite.mentor.web.places.CoursePlace;
import hr.vsite.mentor.web.places.LecturePlace;
import hr.vsite.mentor.web.places.LecturersPlace;
import hr.vsite.mentor.web.services.Api;
import hr.vsite.mentor.web.theme.Theme;

import gwt.material.design.addins.client.sideprofile.MaterialSideProfile;
import gwt.material.design.client.base.HasProgress;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.ProgressType;
import gwt.material.design.client.constants.SideNavType;
import gwt.material.design.client.constants.WavesType;
import gwt.material.design.client.ui.MaterialContainer;
import gwt.material.design.client.ui.MaterialHeader;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialNavBar;
import gwt.material.design.client.ui.MaterialNavBrand;
import gwt.material.design.client.ui.MaterialNavSection;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialSearch;
import gwt.material.design.client.ui.MaterialSideNav;
import gwt.material.design.client.ui.MaterialToast;

public class ApplicationShell extends MaterialPanel implements HasProgress {

	static ApplicationShell get() {
		if (instance == null)
			instance = new ApplicationShell();
		return instance;
	}
	
	public static Resources res() { return res; }

	public interface Resources extends ClientBundle {
		@Source(Style.DefaultCss)
		public Style style();
	}

	@CssResource.ImportedWithPrefix("View")
	public interface Style extends CssResource {
	    String DefaultCss = "hr/vsite/mentor/web/views/view.gss";
		String view();
		String sideNavCourse();
		String toc();
	}

	private ApplicationShell() {

		MaterialHeader header = new MaterialHeader();
			mainNavBar = new MaterialNavBar();
			mainNavBar.setActivates(SideNavId);
				MaterialNavBrand navBrand = new MaterialNavBrand("Mentor");
				navBrand.setPaddingLeft(20.0);
				navBrand.setPaddingRight(20.0);
				navBrand.setHref(GWT.getHostPageBaseURL());
			mainNavBar.add(navBrand);
				MaterialNavSection navSection = new MaterialNavSection();
				navSection.setFloat(Float.RIGHT);
					MaterialLink classroomLink = new MaterialLink("Kolegiji", Places.mapper().getToken(new ClassroomPlace()));
					classroomLink.setWaves(WavesType.LIGHT);
					classroomLink.setTextColor(Color.WHITE);
					classroomLink.setTooltip("Pregled dostupnih kolegija");
				navSection.add(classroomLink);
					MaterialLink lecturersLink = new MaterialLink("Predavači", Places.mapper().getToken(new LecturersPlace()));
					lecturersLink.setWaves(WavesType.LIGHT);
					lecturersLink.setTextColor(Color.WHITE);
					lecturersLink.setTooltip("Popis predavača");
				navSection.add(lecturersLink);
					MaterialLink searchLink = new MaterialLink(IconType.SEARCH);
					searchLink.setWaves(WavesType.LIGHT);
					searchLink.setTooltip("Pretraži gradivo");
				navSection.add(searchLink);
			mainNavBar.add(navSection);
		header.add(mainNavBar);
			MaterialNavBar searchNavBar = new MaterialNavBar();
			searchNavBar.setVisible(false);
				MaterialSearch search = new MaterialSearch();
				search.setBackgroundColor(Color.WHITE);
				search.setPlaceholder("Znam što želim");
				search.setShadow(1);
			searchNavBar.add(search);
		header.add(searchNavBar);
			sideNav = new MaterialSideNav(SideNavType.FIXED);
			sideNav.setTop(64);
			sideNav.setId(SideNavId);
			sideNav.setShowOnAttach(false);
				MaterialSideProfile profile = new MaterialSideProfile();
				profile.setResource(Theme.bundle().profileBackgroundImage());
				profile.setHeight("180px");
			sideNav.add(profile);
				MaterialLink sideNavClassroomLink = new MaterialLink("Kolegiji", Places.mapper().getToken(new ClassroomPlace()));
				sideNavClassroomLink.setWaves(WavesType.DEFAULT);
			sideNav.add(sideNavClassroomLink);
				MaterialLink sideNavLecturersLink = new MaterialLink("Predavači", Places.mapper().getToken(new LecturersPlace()));
				sideNavLecturersLink.setWaves(WavesType.DEFAULT);
			sideNav.add(sideNavLecturersLink);
				sideNavCourseLink = new MaterialLink();
				sideNavCourseLink.addStyleName(res.style().sideNavCourse());
				sideNavCourseLink.setVisible(false);
			sideNav.add(sideNavCourseLink);
		header.add(sideNav);
		add(header);

		
		workPanel = new MaterialContainer();
		add(workPanel);
		
//		MaterialFooter footer = new MaterialFooter();
//			MaterialLink vsiteLink = new MaterialLink("VsiTe");
//			vsiteLink.setHref("https://www.vsite.hr/");
//		footer.add(vsiteLink);
//		add(footer);
		
		searchLink.addClickHandler(e -> search.open());
		
		search.addOpenHandler(event -> {
			mainNavBar.setVisible(false);
			searchNavBar.setVisible(true);
		});
		
		search.addCloseHandler(event -> {
			mainNavBar.setVisible(true);
			searchNavBar.setVisible(false);
		});
		
		search.addValueChangeHandler(e -> {
			MaterialToast.fireToast("TODO: Pretraga pojma \"" + e.getValue() + "\"");
			mainNavBar.setVisible(true);
			searchNavBar.setVisible(false);
		});
		
		// TODO instead of subscribing to this event which required further resolving,
		// implement new events (InitializedPlace<Something>) throw them in views and and subscribe to them here
		MentorBus.get().addHandler(PlaceChangeEvent.TYPE, e -> onPlaceChanged(e.getNewPlace()));
		onPlaceChanged(Places.controller().getWhere());

	}

	protected IsWidget wrap(IsWidget widget) {
		workPanel.clear();
		workPanel.add(widget);
		return this;
	}

	@Override
	public void showProgress(ProgressType type) {
		mainNavBar.showProgress(type);
	}

	@Override
	public void setPercent(double percent) {
		mainNavBar.setPercent(percent);
	}

	@Override
	public void hideProgress() {
		mainNavBar.hideProgress();
	}

	private void onPlaceChanged(Place newPlace) {
		sideNavCourseLink.setVisible(false);
		if (newPlace instanceof LecturePlace) {
			if (courseRequest != null && courseRequest.isPending())
				courseRequest.cancel();
			courseRequest = Api.get().course().findById(((LecturePlace) newPlace).getCourseId(), new MethodCallback<Course>() {
				@Override
				public void onSuccess(Method method, Course course) {
					if (course == null) {
						MaterialToast.fireToast("Couldn't load course!");
						return;
					}
					sideNavCourseLink.setText(course.getTitle());
					sideNavCourseLink.setHref(Places.mapper().getToken(new CoursePlace(course.getId())));
					sideNavCourseLink.setVisible(true);
				}
				@Override
				public void onFailure(Method method, Throwable exception) {
					MaterialToast.fireToast("Couldn't load course! " + exception);
				}
			});
		}
	}

	private static final String SideNavId = "mentor-sidenav";
	
	private final MaterialNavBar mainNavBar;
	private final MaterialSideNav sideNav;
	private final MaterialLink sideNavCourseLink;
	private final MaterialContainer workPanel;

	private Request courseRequest = null;
	
	private static ApplicationShell instance = null;
	private static final Resources res = GWT.create(Resources.class);
	static {
		res.style().ensureInjected();
	}

}

