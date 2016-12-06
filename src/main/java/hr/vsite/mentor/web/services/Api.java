package hr.vsite.mentor.web.services;

import org.fusesource.restygwt.client.Resource;
import org.fusesource.restygwt.client.RestServiceProxy;

import com.google.gwt.core.client.GWT;

public class Api {

	public static Api get() {
		if (instance == null)
			instance = new Api();
		return instance;
	}
	
	private Api() {}

	public CourseService course() {
		if (courseService == null) {
			courseService = GWT.create(CourseService.class);
			((RestServiceProxy) courseService).setResource(getResource("/courses"));
		}
		return courseService;
	}
	
	public LectureService lecture() {
		if (lectureService == null) {
			lectureService = GWT.create(LectureService.class);
			((RestServiceProxy) lectureService).setResource(getResource("/lectures"));
		}
		return lectureService;
	}
	
	public UnitService unit() {
		if (unitService == null) {
			unitService = GWT.create(UnitService.class);
			((RestServiceProxy) unitService).setResource(getResource("/units"));
		}
		return unitService;
	}
	
	private Resource getResource(String path) {
		return new Resource(GWT.getModuleBaseURL() + "../api" + (path.startsWith("/") ? "" : "/") + path);
	}
	
	private static Api instance = null;
	
	private CourseService courseService = null;
	private LectureService lectureService = null;
	private UnitService unitService = null;
	
}
