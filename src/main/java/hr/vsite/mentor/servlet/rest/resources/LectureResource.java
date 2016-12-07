package hr.vsite.mentor.servlet.rest.resources;

import java.nio.file.Files;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import hr.vsite.mentor.course.Course;
import hr.vsite.mentor.lecture.Lecture;
import hr.vsite.mentor.lecture.LectureFilter;
import hr.vsite.mentor.lecture.LectureManager;
import hr.vsite.mentor.unit.Unit;
import hr.vsite.mentor.user.User;

@Path("lectures")
public class LectureResource {
	
	@Inject
	public LectureResource(Provider<LectureManager> lectureProvider) {
		this.lectureProvider = lectureProvider;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public List<Lecture> list(
		@QueryParam("title") String title,
		@QueryParam("author") User author,
		@QueryParam("course") Course course,
		@QueryParam("count") Integer count,
		@QueryParam("offset") Integer offset
	) {		
		LectureFilter filter = new LectureFilter();
		filter.setTitle(title);
		filter.setAuthor(author);
		filter.setCourse(course);
		
		return lectureProvider.get().list(filter, count, offset);	
	}
   
	@GET
	@Path("{lectureId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Lecture findById(@PathParam("lectureId") Lecture lectureId) {

		if (lectureId == null)
			throw new NoSuchElementException("Unable to resolve Lecture, please provide other lecture_id !!");

		return lectureId;

	}
	
	@GET
	@Path("{lectureId}/headUnit")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Unit getHeadUnit(@PathParam("lectureId") Lecture lecture){
		
		if(lecture == null)
			throw new NoSuchElementException("Unable to resolve lectureHeadUnit, please provide other lecture_id !!");
		
		Unit headUnit = lectureProvider.get().getHeadUnit(lecture.getId());
		if(headUnit == null)
			throw new NoSuchElementException("Lecture doesn't have headUnit");
		
		return headUnit;
	}
	
	@GET
	@Path("{lectureId}/thumbnail")
	@Transactional
		
		if(lecture == null)
			throw new NoSuchElementException("Unable to resolve thumbnail, please provide other lecture_id !!");
		
		Unit headUnit = lectureProvider.get().getHeadUnit(lecture.getId());
		
		if(headUnit != null){
			java.nio.file.Path path = headUnit.getThumbnailPath();
			if (path != null && Files.exists(path))
				return Response.ok(path.toFile(), "image/jpeg").build();
		}
		
		return Response.ok(ClassLoader.getSystemResourceAsStream("lecture.jpg"), "image/jpeg").build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional
	public Response insert(Lecture lecture){
				
		return Response.status(201).entity(lectureProvider.get().insert(lecture)).build();
	}
	
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional
	public Response update(@QueryParam("lecture") Lecture lectureBefore, Lecture lectureAfter){
		
		if(lectureBefore == null || lectureBefore.getId() != lectureAfter.getId())
			throw new NoSuchElementException("Unable to update Lecture, please provide other lecture_id !!");

		return Response.status(200).entity(lectureProvider.get().update(lectureBefore.getId(), lectureAfter)).build();
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Response delete(@QueryParam("lecture") Lecture lecture){	
		
		if(lecture == null)
			throw new NoSuchElementException("Unable to delete Lecture, please provide other lecture_id !!");
		
		return Response.status(200).entity(lectureProvider.get().delete(lecture)).build();
	}


	private final Provider<LectureManager> lectureProvider;
}