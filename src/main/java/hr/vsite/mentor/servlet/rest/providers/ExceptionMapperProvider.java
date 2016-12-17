package hr.vsite.mentor.servlet.rest.providers;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hr.vsite.mentor.servlet.rest.MentorError;

@Provider
public class ExceptionMapperProvider implements ExceptionMapper<Throwable> {

	@Override
	public Response toResponse(Throwable exception) {

		MentorError error = new MentorError();
		
		// TODO using HTTP response code as our error level is a redundancy
		if (WebApplicationException.class.isInstance(exception))
			// WebApplicationException derivatives do define HTTP response codes themselves, so use them
			error.setCode(WebApplicationException.class.cast(exception).getResponse().getStatus());
		else
			// Not a WebApplicationException? We fucked up something seriously.
			error.setCode(500);
		
		StringBuilder messageBuilder = new StringBuilder(1000);
		messageBuilder.append(exception.getClass().getName());
		if (exception.getMessage() != null) 
			messageBuilder.append(": " + exception.getMessage());
		error.setMessage(messageBuilder.toString());
		
		if (ClientErrorException.class.isInstance(exception))
			Log.warn("Client API error: {}", error.getMessage());
		else
			Log.error("Unhandled API exception", exception);
			
		return Response.status(error.getCode()).type(MediaType.APPLICATION_JSON_TYPE).entity(error).build();
		
	}

	private static final Logger Log = LoggerFactory.getLogger(ExceptionMapperProvider.class);

}
