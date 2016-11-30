package hr.vsite.mentor.web.services;

import java.util.List;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.QueryParam;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import com.google.gwt.http.client.Request;

import hr.vsite.mentor.lecture.Lecture;

public interface LectureService extends RestService {

	@GET
	public Request list(
		@QueryParam("title") String title,
		@QueryParam("author") UUID authorId,
		@QueryParam("course") UUID courseId,
		@QueryParam("count") Integer count,
		@QueryParam("offset") Integer offset,
		MethodCallback<List<Lecture>> callback);

//	@GET
//	@Path("{client}/roads")
//	public Request roads(@PathParam("client") String clientMark, MethodCallback<Set<Road>> callback);
//
//	@GET
//	@Path("{client}/counties")
//	public Request counties(@PathParam("client") String clientMark, MethodCallback<Set<County>> callback);
//
//	@GET
//	@Path("{client}/sectionSignProgressReport")
//	public Request sectionSignProgressReport(@PathParam("client") String clientMark, MethodCallback<SectionSignProgressReport> callback);

}