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

import hr.vsite.mentor.user.User;
import hr.vsite.mentor.user.UserFilter;
import hr.vsite.mentor.user.UserManager;

@Path("user")
public class UserResource {

	@Inject
	public UserResource(Provider<UserManager> userProvider) {
		this.userProvider = userProvider;
	}
	
	@GET
	@Path("list")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public List<User> list(
		@QueryParam("name") String name,
		@QueryParam("email") String email,
		@QueryParam("count") Integer count,
		@QueryParam("offset") Integer offset
	) {

		UserFilter filter = new UserFilter();
		filter.setName(name);
		filter.setEmail(email);
		
		return userProvider.get().list(filter, count, offset);
		
	}
   
	@GET
	@Path("{user}")
	@Produces(MediaType.APPLICATION_JSON)
	public User findById(
		@PathParam("user") User user
	) {

		if (user == null)
			throw new NotFoundException();

		return user;

	}

	private final Provider<UserManager> userProvider;
    
}