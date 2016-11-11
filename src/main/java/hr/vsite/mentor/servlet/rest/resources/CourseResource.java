package hr.vsite.mentor.servlet.rest.resources;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import hr.vsite.mentor.course.Course;
import hr.vsite.mentor.course.CourseFilter;
import hr.vsite.mentor.course.CourseManager;
import hr.vsite.mentor.user.User;

@Path("course")
public class CourseResource {

	@Inject
	public CourseResource(Provider<CourseManager> courseProvider) {
		this.courseProvider = courseProvider;
	}
	
	@GET
	@Path("list")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public List<Course> list(
		@QueryParam("title") String title,
		@QueryParam("author") User author,
		@QueryParam("count") Integer count,
		@QueryParam("offset") Integer offset
	) {

		CourseFilter filter = new CourseFilter();
		filter.setTitle(title);
		filter.setAuthor(author);
		
		return courseProvider.get().list(filter, count, offset);
		
	}
   
	@GET
	@Path("{course}")
	@Produces(MediaType.APPLICATION_JSON)
	public Course findById(
		@PathParam("course") Course course
	) {

		if (course == null)
			throw new NotFoundException();

		return course;

	}

	private final Provider<CourseManager> courseProvider;
    
}