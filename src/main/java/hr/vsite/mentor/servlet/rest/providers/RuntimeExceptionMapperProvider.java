package hr.vsite.mentor.servlet.rest.providers;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.google.inject.Inject;

import hr.vsite.mentor.error.MentorError;

@Provider
public class RuntimeExceptionMapperProvider implements ExceptionMapper<RuntimeException>{

	@Inject
	RuntimeExceptionMapperProvider(MentorError error) {
		this.error = error;
	}
	
	@Override
	public Response toResponse(RuntimeException exception) {

		if(exception.getCause() != null) {
			error.setUserMessage(exception.getMessage());
			error.setInternalMessage(exception.getCause().toString());
		} else {
			error.setUserMessage("General error");
			error.setInternalMessage(exception.getMessage());
		}
		error.setCode(-1);
		error.setInfo("None");
		return Response.status(400).entity(error).build();
	}

	private MentorError error;
}
