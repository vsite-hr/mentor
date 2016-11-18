package hr.vsite.mentor.servlet.rest.providers;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.spi.ReaderException;

import com.google.inject.Inject;

import hr.vsite.mentor.error.MentorError;

@Provider
public class ReaderExceptionMapperProvider implements ExceptionMapper<ReaderException>{

	@Inject
	ReaderExceptionMapperProvider(MentorError error) {
		this.error = error;
	}
	
	@Override
	public Response toResponse(ReaderException exception) {

		error.setUserMessage("Error reading incomming JSON data");
		error.setInternalMessage(exception.getMessage());
		error.setCode(exception.getErrorCode());
		error.setInfo("http://www.ecma-international.org/publications/standards/Ecma-404.htm");
		return Response.status(400).entity(error).build();
	}

	private MentorError error;
}
