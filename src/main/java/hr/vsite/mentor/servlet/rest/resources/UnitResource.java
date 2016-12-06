package hr.vsite.mentor.servlet.rest.resources;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import hr.vsite.mentor.lecture.Lecture;
import hr.vsite.mentor.servlet.rest.NioMediaStreamer;
import hr.vsite.mentor.unit.Unit;
import hr.vsite.mentor.unit.UnitFilter;
import hr.vsite.mentor.unit.UnitManager;
import hr.vsite.mentor.unit.VideoUnit;
import hr.vsite.mentor.user.User;

@Path("units")
public class UnitResource {

	@Inject
	public UnitResource(Provider<UnitManager> unitProvider) {
		this.unitProvider = unitProvider;
	}
	
	@GET
	@Path("list")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public List<Unit> list(
		@QueryParam("type") Unit.Type type,
		@QueryParam("title") String title,
		@QueryParam("author") User author,
		@QueryParam("lecture") Lecture lecture,
		@QueryParam("count") Integer count,
		@QueryParam("offset") Integer offset
	) {

		UnitFilter filter = new UnitFilter();
		filter.setType(type);
		filter.setTitle(title);
		filter.setAuthor(author);
		filter.setLecture(lecture);
		
		return unitProvider.get().list(filter, count, offset);
		
	}
   
	@GET
	@Path("{unit}")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Unit findById(
		@PathParam("unit") Unit unit
	) {

		if (unit == null)
			throw new NotFoundException();
		
		return unit;

	}

	@GET
	@Path("{unit}/thumbnail")
	public Response photo(
		@PathParam("unit") Unit unit
	) {

		if (unit == null)
			throw new NotFoundException();

		java.nio.file.Path path = unit.getThumbnailPath();
		if (path != null && Files.exists(path))
			return Response.ok(path.toFile(), "image/jpeg").build();
		
		// TODO if unit is of external type and we can redirect to another url, make it so
		
		return Response.ok(ClassLoader.getSystemResourceAsStream("unit.jpg"), "image/jpeg").build();

	}
	
	@HEAD
	@Path("{unit}/content")
	public Response contentHead(
		@PathParam("unit") Unit unit
	) throws IOException {
		
		if (unit == null)
			throw new NotFoundException();

		if (VideoUnit.class.isInstance(unit)) {
			java.nio.file.Path path = VideoUnit.class.cast(unit).getVideoPath();
			if (path == null || !Files.exists(path))
				throw new WebApplicationException("Missing content for MentorVideoUnit " + unit);
			return Response.ok(null, "video/mp4").status(206)
	        	.header(HttpHeaders.CONTENT_LENGTH, Files.size(path))
				.header(HttpHeaders.LAST_MODIFIED, new Date(Files.getLastModifiedTime(path).toMillis()))
	        	.build();
		}

		// TODO if unit is of external type and we can redirect to another url, make it so
		
		throw new NotAcceptableException("Unsupported HEAD request for content of unit " + unit + " (" + unit.getType() + ")");
		
    }

	@GET
	@Path("{unit}/content")
	public Response content(
			@PathParam("unit") Unit unit,
			@HeaderParam("Range") String range
	) throws IOException {

		if (unit == null)
			throw new NotFoundException();

		if (VideoUnit.class.isInstance(unit)) {
			java.nio.file.Path path = VideoUnit.class.cast(unit).getVideoPath();
			if (path == null || !Files.exists(path))
				throw new WebApplicationException("Missing content for MentorVideoUnit " + unit);
			long length = Files.size(path);
			long from, to;
			if (range != null) {
				// stream partial file
				String[] ranges = range.split("=")[1].split("-");
				from = Long.parseLong(ranges[0]);
				if (ranges.length == 2) {
					to = Long.parseLong(ranges[1]);
					if (to >= length)
						to = length - 1;
				} else {
					to = length - 1;
				}
			} else {
				// stream whole file
				from = 0;
				to = length - 1;
			}
			
			String responseRange = String.format("bytes %d-%d/%d", from, to, length);
			NioMediaStreamer streamer = new NioMediaStreamer(path, from, to);
	
			return Response.ok(streamer, "video/mp4").status(206)
				.header("Accept-Ranges", "bytes")
				.header("Content-Range", responseRange)
				.header(HttpHeaders.CONTENT_LENGTH, streamer.getLenth())
				.header(HttpHeaders.LAST_MODIFIED, new Date(Files.getLastModifiedTime(path).toMillis()))
				.build();
		}
			
		// TODO if unit is of external type and we can redirect to another url, make it so
		
		throw new NotAcceptableException("Unsupported GET request for content of unit " + unit + " (" + unit.getType() + ")");
		
	}
	
	private final Provider<UnitManager> unitProvider;
    
}