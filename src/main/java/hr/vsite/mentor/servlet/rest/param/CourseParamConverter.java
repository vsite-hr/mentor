package hr.vsite.mentor.servlet.rest.param;

import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.transaction.Transactional;
import javax.ws.rs.ext.ParamConverter;

import org.apache.commons.lang3.StringUtils;

import hr.vsite.mentor.course.Course;
import hr.vsite.mentor.course.CourseManager;

public class CourseParamConverter implements ParamConverter<Course> {

	@Inject
	CourseParamConverter(Provider<CourseManager> courseProvider) {
		this.courseProvider = courseProvider;
	}
	
	@Override
	@Transactional
	public Course fromString(String id) {

		if (StringUtils.isBlank(id))
			return null;
		
		return courseProvider.get().findById(UUID.fromString(id));
		
	}

	@Override
	public String toString(Course course) {
		if (course == null)
			return null;
		return course.getId().toString();
	}

	private final Provider<CourseManager> courseProvider;
	
}
