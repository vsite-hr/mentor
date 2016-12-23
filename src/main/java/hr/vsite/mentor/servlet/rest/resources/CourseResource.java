package hr.vsite.mentor.servlet.rest.resources;

import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.transaction.Transactional;
import javax.ws.rs.ClientErrorException;
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
import hr.vsite.mentor.lecture.Lecture;
import hr.vsite.mentor.lecture.LectureManager;
import hr.vsite.mentor.unit.Unit;
import hr.vsite.mentor.user.User;

@Path("courses")
public class CourseResource {

	@Inject
	public CourseResource(Provider<CourseManager> courseProvider, Provider<LectureManager> lectureProvider) {
		this.courseProvider = courseProvider;
		this.lectureProvider = lectureProvider;
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
			throw new NotFoundException("Course not found");
		return course;
	}
	
	@GET
	@Path("{course}/head")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Response getHeadUnit(@PathParam("course") Course course) {
		if(course == null)
			throw new NotFoundException("Course not found");
		Unit headUnit = courseProvider.get().getHeadUnit(course);
		if(headUnit == null)
			return Response.status(204).build();
		return Response.status(200).entity(headUnit).build();
	}
	
	@GET
	@Path("{course}/thumbnail")
	@Transactional
	public Response photo(@PathParam("course") Course course) {
		if(course == null)
			throw new NotFoundException("Course not found");
		Unit headUnit = courseProvider.get().getHeadUnit(course);	
		if(headUnit != null) {
			java.nio.file.Path path = headUnit.getThumbnailPath();
			if (path != null && Files.exists(path))
				return Response.ok(path.toFile(), "image/jpeg").build();
		}		
		return Response.ok(ClassLoader.getSystemResourceAsStream("unit.jpg"), "image/jpeg").build();
	}
	
	@GET
	@Path("{course}/lectures")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Response getLectures(@PathParam("course") Course course) {
		if(course == null)
			throw new NotFoundException("Course not found");
		return Response.status(200).entity(lectureProvider.get().list(course)).build();
	}
	
	@GET
	@Path("{course}/lectures/count")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public int getLecturesCount(@PathParam("course") Course course) {
		if(course == null)
			throw new NotFoundException("Course not found");
		return lectureProvider.get().count(course);
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
	public Response update(@PathParam("id") UUID id, Course newValues) {
		if(!id.equals(newValues.getId()))
			throw new ClientErrorException("Course ID error", 400);
		return Response.status(200).entity(courseProvider.get().update(newValues)).build();
	}
	
	@DELETE
	@Path("{id}")
	@Transactional
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") UUID id, Course course) {
		if(!id.equals(course.getId()))
			throw new ClientErrorException("Course ID error", 400);
		return Response.status(200).entity(courseProvider.get().delete(course)).build();
	}
	private final Provider<CourseManager> courseProvider;
	private final Provider<LectureManager> lectureProvider;
}