package hr.vsite.mentor.servlet.rest.resources;

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

import hr.vsite.mentor.lecture.Lecture;
import hr.vsite.mentor.unit.Unit;
import hr.vsite.mentor.unit.UnitFilter;
import hr.vsite.mentor.unit.UnitManager;
import hr.vsite.mentor.unit.UnitType;
import hr.vsite.mentor.user.User;

@Path("unit")
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
		@QueryParam("type") UnitType type,
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

	private final Provider<UnitManager> unitProvider;
    
}