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

import hr.vsite.mentor.lecture.Lecture;
import hr.vsite.mentor.unit.Unit;
import hr.vsite.mentor.unit.UnitFilter;
import hr.vsite.mentor.unit.UnitManager;
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
			return Response.ok(path.toFile()).build();
		
		// TODO if unit is of external type and we can redirect to another url that holds thumbnail, make it so
		
		return Response.ok(ClassLoader.getSystemResourceAsStream("unit.jpg"), "image/jpeg").build();

	}
	
	private final Provider<UnitManager> unitProvider;
    
}