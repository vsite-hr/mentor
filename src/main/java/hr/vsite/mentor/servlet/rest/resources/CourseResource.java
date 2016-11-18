package hr.vsite.mentor.servlet.rest.resources;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import hr.vsite.mentor.course.Course;
import hr.vsite.mentor.course.CourseFilter;
import hr.vsite.mentor.course.CourseManager;
import hr.vsite.mentor.user.User;

@Path("courses")
public class CourseResource {

	@Inject
	public CourseResource(Provider<CourseManager> courseProvider) {
		this.courseProvider = courseProvider;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public List<Course> list(@QueryParam("title") String title, @QueryParam("author") User author,
			@QueryParam("count") Integer count, @QueryParam("offset") Integer offset) {

		CourseFilter filter = new CourseFilter();
		filter.setTitle(title);
		filter.setAuthor(author);

		return courseProvider.get().list(filter, count, offset);

	}

	@GET
	@Path("{course}")
	@Produces(MediaType.APPLICATION_JSON)
	public Course findById(@PathParam("course") Course course) {

		if (course == null)
			throw new NotFoundException();

		return course;

	}

	@POST
	@Transactional
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(Course course) {
		return Response.status(201).entity(courseProvider.get().insert(course)).build();
	}

	@PUT
	@Path("{id}")
	@Transactional
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") UUID idToUpdate, Course newValues) {
		newValues.setId(idToUpdate);
		return Response.status(200).entity(courseProvider.get().update(newValues)).build();
	}
	
	@DELETE
	@Path("{course}")
	@Transactional
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("course") UUID idToDelete) {
		courseProvider.get().delete(idToDelete);
		return Response.status(204).build();
	}

	private final Provider<CourseManager> courseProvider;

}