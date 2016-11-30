package hr.vsite.mentor.servlet.rest.resources;

import java.nio.file.Files;
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
import javax.ws.rs.core.Response;

import hr.vsite.mentor.user.User;
import hr.vsite.mentor.user.UserFilter;
import hr.vsite.mentor.user.UserManager;

@Path("users")
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

	@GET
	@Path("{user}/photo")
	@Produces("image/jpeg")
	public Response photo(
		@PathParam("user") User user
	) {

		if (user == null)
			throw new NotFoundException();

		java.nio.file.Path path = user.getPhotoPath();
		if (Files.exists(path))
			return Response.ok(path.toFile()).build();
		
		return Response.ok(ClassLoader.getSystemResourceAsStream("silhouette.jpg")).build();
//		return Response.ok(getClass().getClassLoader().getResourceAsStream("silhouette.jpg")).build();

	}

	private final Provider<UserManager> userProvider;
    
}