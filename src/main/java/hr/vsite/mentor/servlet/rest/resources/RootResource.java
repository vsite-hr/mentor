package hr.vsite.mentor.servlet.rest.resources;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import hr.vsite.mentor.MentorStatus;

@Path("")
public class RootResource {

	@Inject
	public RootResource(Provider<MentorStatus> statusProvider) {
		this.statusProvider = statusProvider;
	}
	
	@GET
	@Path("status")
	@Produces(MediaType.APPLICATION_JSON)
	public MentorStatus status() {
		return statusProvider.get();
	}
   
	@GET
	@Path("ip")
	@Produces(MediaType.TEXT_PLAIN)
	public String ip(@Context HttpServletRequest request) {
		return request.getRemoteAddr();
	}

	private final Provider<MentorStatus> statusProvider;
    
}