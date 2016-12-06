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

import hr.vsite.mentor.unit.Unit;

public interface UnitService extends RestService {

	@GET
	public Request list(
		@QueryParam("type") Unit.Type type,
		@QueryParam("title") String title,
		@QueryParam("author") UUID authorId,
		@QueryParam("lecture") UUID lectureId,
		@QueryParam("count") Integer count,
		@QueryParam("offset") Integer offset,
		MethodCallback<List<Unit>> callback);

	@GET
	@Path("{unit}")
	public Request findById(@PathParam("unit") UUID unitId, MethodCallback<Unit> callback);

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