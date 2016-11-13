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
import hr.vsite.mentor.lecture.Lecture;
import hr.vsite.mentor.lecture.LectureFilter;
import hr.vsite.mentor.lecture.LectureManager;
import hr.vsite.mentor.user.User;

@Path("lecture")
public class LectureResource {
	
	@Inject
	public LectureResource(Provider<LectureManager> lectureProvider) {
		this.lectureProvider = lectureProvider;
	}
	
	@GET
	@Path("list")
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
	@Path("{lecture_id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Lecture findById(@PathParam("lecture_id") Lecture lecture_id) {

		if (lecture_id == null)
			throw new NotFoundException();

		return lecture_id;

	}

	private final Provider<LectureManager> lectureProvider;
}