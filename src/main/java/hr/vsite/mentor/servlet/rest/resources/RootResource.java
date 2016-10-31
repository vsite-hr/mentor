package hr.vsite.mentor.servlet.rest.resources;

import javax.inject.Inject;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.google.inject.Injector;

import hr.vsite.mentor.MentorStatus;

@Path("")
public class RootResource {

	@Inject
	public RootResource(Injector injector) {
		this.injector = injector;
	}
	
	@GET
	@Path("status")
	@Produces(MediaType.APPLICATION_JSON)
	public MentorStatus status() {
		return injector.getInstance(MentorStatus.class);
	}
   
	@GET
	@Path("ip")
	@Produces(MediaType.TEXT_PLAIN)
	public String ip(@Context HttpServletRequest request) {
		return request.getRemoteAddr();
	}

	private final Injector injector;
    
}