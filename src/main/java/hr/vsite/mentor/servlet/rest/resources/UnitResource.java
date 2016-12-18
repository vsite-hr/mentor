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
import hr.vsite.mentor.unit.ImageUnit;
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
	public Response thumbnail(
		@PathParam("unit") Unit unit
	) {

		if (unit == null)
			throw new NotFoundException();

		java.nio.file.Path path = unit.getThumbnailPath();
		if (path != null && Files.exists(path))
			return Response.ok(path.toFile(), unit.getThumbnailContentType()).build();

		// TODO if unit is of external type and we can redirect to another URL, make it so

		return Response.ok(ClassLoader.getSystemResourceAsStream("unit.jpg"), "image/jpeg").build();

	}
	
	@HEAD
	@Path("{unit}/content")
	public Response contentHead(
		@PathParam("unit") Unit unit
	) throws IOException {
		
		if (unit == null)
			throw new NotFoundException();

		switch (unit.getType()) {
		
			case Video: {
				VideoUnit videoUnit = VideoUnit.class.cast(unit);
				java.nio.file.Path path = videoUnit.getVideoPath();
				if (path == null || !Files.exists(path))
					throw new WebApplicationException("Missing content for VideoUnit " + unit);
				return Response.ok(null, videoUnit.getVideoContentType()).status(206)
		        	.header(HttpHeaders.CONTENT_LENGTH, Files.size(path))
					.header(HttpHeaders.LAST_MODIFIED, new Date(Files.getLastModifiedTime(path).toMillis()))
		        	.build();
			}
			
			case Image: {
				ImageUnit imageUnit = ImageUnit.class.cast(unit);
				java.nio.file.Path path = imageUnit.getImagePath();
				if (path == null || !Files.exists(path))
					throw new WebApplicationException("Missing content for ImageUnit " + unit);
				return Response.ok(null, imageUnit.getImageContentType()).status(206)
		        	.header(HttpHeaders.CONTENT_LENGTH, Files.size(path))
					.header(HttpHeaders.LAST_MODIFIED, new Date(Files.getLastModifiedTime(path).toMillis()))
		        	.build();
			}
				
			default:
				throw new NotAcceptableException("Unsupported HEAD request for unit's " + unit + " content (" + unit.getType() + ")");
				
		}
		
		// TODO if unit is of external type and we can redirect to another URL, make it so
		
		
    }

	@GET
	@Path("{unit}/content")
	public Response content(
			@PathParam("unit") Unit unit,
			@HeaderParam("Range") String range
	) throws IOException {

		if (unit == null)
			throw new NotFoundException();

		switch (unit.getType()) {
		
			case Video: {

				VideoUnit videoUnit = VideoUnit.class.cast(unit);
				java.nio.file.Path path = videoUnit.getVideoPath();
				if (path == null || !Files.exists(path))
					throw new WebApplicationException("Missing content for VideoUnit " + unit);
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
		
				return Response.ok(streamer, videoUnit.getVideoContentType()).status(range != null ? 206 : 200)
					.header("Accept-Ranges", "bytes")
					.header("Content-Range", responseRange)
					.header(HttpHeaders.CONTENT_LENGTH, streamer.getLength())
					.header(HttpHeaders.LAST_MODIFIED, new Date(Files.getLastModifiedTime(path).toMillis()))
					.build();

			}

			case Image: {

				ImageUnit imageUnit = ImageUnit.class.cast(unit);
				java.nio.file.Path path = imageUnit.getImagePath();
				if (path == null || !Files.exists(path))
					throw new WebApplicationException("Missing content for ImageUnit " + unit);
				long length = Files.size(path);

				return Response.ok(path.toFile(), imageUnit.getImageContentType())
					.header(HttpHeaders.CONTENT_LENGTH, length)
					.header(HttpHeaders.LAST_MODIFIED, new Date(Files.getLastModifiedTime(path).toMillis()))
					.build();

			}
			
			default:
				throw new NotAcceptableException("Unsupported GET request for content of unit " + unit + " (" + unit.getType() + ")");
				
		}
			
		// TODO if unit is of external type and we can redirect to another URL, make it so
		
	}
	
	private final Provider<UnitManager> unitProvider;
    
}