package hr.vsite.mentor.servlet.rest.providers;

import java.util.NoSuchElementException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.google.inject.Inject;

import hr.vsite.mentor.error.MentorError;

@Provider
public class NoSuchElementExceptionMapperProvider implements ExceptionMapper<NoSuchElementException>{

	@Inject
	NoSuchElementExceptionMapperProvider(MentorError error) {
		this.error = error;
	}
	
	@Override
	public Response toResponse(NoSuchElementException exception) {

		error.setUserMessage("No such element");
		error.setInternalMessage(exception.getMessage());
		error.setCode(404);
		error.setInfo("None");
		return Response.status(404).entity(error).build();
	}

	private MentorError error;
}
