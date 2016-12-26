package hr.vsite.mentor.web.services;

import java.util.List;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import com.google.gwt.http.client.Request;

import hr.vsite.mentor.lecture.Lecture;
import hr.vsite.mentor.unit.Unit;

public interface LectureService extends RestService {

	@GET
	public Request list(
		@QueryParam("title") String title,
		@QueryParam("author") UUID authorId,
		@QueryParam("course") UUID courseId,
		@QueryParam("count") Integer count,
		@QueryParam("offset") Integer offset,
		MethodCallback<List<Lecture>> callback);

	@GET
	@Path("{lecture}")
	public Request findById(@PathParam("lecture") UUID lectureId, MethodCallback<Lecture> callback);

	@GET
	@Path("{lecture}/head")
	public Request getHeadUnit(@PathParam("lecture") UUID lectureId, MethodCallback<Unit> callback);

	@GET
	@Path("{lecture}/units")
	public Request getUnits(@PathParam("lecture") UUID lectureId, MethodCallback<List<Unit>> callback);

	@GET
	@Path("{lecture}/units/count")
	public Request getUnitsCount(@PathParam("lecture") UUID lectureId, MethodCallback<Integer> callback);

}