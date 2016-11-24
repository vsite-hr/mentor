package hr.vsite.mentor.servlet.rest.providers;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import hr.vsite.mentor.error.MentorError;

@Provider
public class ExceptionMapperProvider implements ExceptionMapper<Exception> {

	@Override
	public Response toResponse(Exception exception) {

		if(exception instanceof JsonParseException || 
				exception instanceof JsonMappingException ||
				exception instanceof BadRequestException) {
			error.setCode(400);
		} else if(exception instanceof NotFoundException) {
			error.setCode(404);
		} else if(exception instanceof InternalServerErrorException) {
			error.setCode(500);
		}
		String message = (exception.getCause()) == null ? 
				exception.getMessage() : 
				exception.getMessage() + ", " + exception.getCause().getMessage();
		error.setMessage(message);
		Log.debug(message);
		return Response.status(error.getCode()).entity(error).build();
	}

	private MentorError error = new MentorError();
	private static final Logger Log = LoggerFactory.getLogger(ExceptionMapperProvider.class);
}
