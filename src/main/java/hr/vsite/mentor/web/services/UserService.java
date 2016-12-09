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

import hr.vsite.mentor.user.User;

public interface UserService extends RestService {

	@GET
	public Request list(
		@QueryParam("name") String name,
		@QueryParam("email") String email,
		@QueryParam("count") Integer count,
		@QueryParam("offset") Integer offset,
		MethodCallback<List<User>> callback);

	@GET
	@Path("{user}")
	public Request findById(@PathParam("user") UUID userId, MethodCallback<User> callback);

//	@GET
//	@Path("{client}/counties")
//	public Request counties(@PathParam("client") String clientMark, MethodCallback<Set<County>> callback);
//
//	@GET
//	@Path("{client}/sectionSignProgressReport")
//	public Request sectionSignProgressReport(@PathParam("client") String clientMark, MethodCallback<SectionSignProgressReport> callback);

}