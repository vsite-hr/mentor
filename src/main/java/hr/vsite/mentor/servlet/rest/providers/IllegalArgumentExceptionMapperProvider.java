package hr.vsite.mentor.servlet.rest.providers;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.google.inject.Inject;

import hr.vsite.mentor.error.MentorError;

@Provider
public class IllegalArgumentExceptionMapperProvider implements ExceptionMapper<IllegalArgumentException>{

	@Inject
	IllegalArgumentExceptionMapperProvider(MentorError error) {
		this.error = error;
	}
	
	@Override
	public Response toResponse(IllegalArgumentException exception) {

		error.setUserMessage("Illegal arguments");
		error.setInternalMessage(exception.getMessage());
		error.setCode(400);
		error.setInfo("None");
		return Response.status(400).entity(error).build();
	}

	private MentorError error;
}
