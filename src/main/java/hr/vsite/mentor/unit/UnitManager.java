package hr.vsite.mentor.unit;

import java.io.IOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Provider;

import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import hr.vsite.mentor.db.JdbcUtils;
import hr.vsite.mentor.lecture.Lecture;
import hr.vsite.mentor.user.UserManager;

public class UnitManager {

	@Inject
	public UnitManager(Provider<UserManager> userProvider, Provider<Connection> connProvider, ObjectMapper mapper) {
		this.userProvider = userProvider;
		this.connProvider = connProvider;
		this.mapper = mapper;
	}

	/** Returns <code>Unit</code> with corresponding id, or <code>null</code> if such unit does not exist. */
	public Unit findById(UUID id) {
		
	    try (PreparedStatement statement = connProvider.get().prepareStatement("SELECT * FROM units WHERE unit_id = ?")) {
		    statement.setObject(1, id);
	        try (ResultSet resultSet = statement.executeQuery()) {
	        	return resultSet.next() ? fromResultSet(resultSet) : null;
	        }
		} catch (SQLException e) {
			throw new RuntimeException("Unable to find unit with id " + id, e);
		}
	    
	}
	
	/** Returns all units known to application, unpaged. If there is a possibility for large number of units,
	 * use {@link #list(UnitFilter, Integer, Integer)}*/
	public List<Unit> list() {
		return list(new UnitFilter(), null, null);
	}
	
	/** Returns units that match given criteria, unpaged. If there is a possibility for large number of units,
	 * use {@link #list(UnitFilter, Integer, Integer)}*/
	public List<Unit> list(UnitFilter filter) {
		return list(filter, null, null);
	}
	
	/** Returns units that match given criteria, paged.*/
	public List<Unit> list(UnitFilter filter, Integer count, Integer offset) {
		
		List<Unit> units = new ArrayList<>(count != null ? count : 10);
		
		StringBuilder queryBuilder = new StringBuilder(1000);
		queryBuilder.append("SELECT * FROM units WHERE true");
		if (filter.getType() != null)
			queryBuilder.append(" AND unit_type = ?::unit_type");
		if (filter.getTitle() != null)
			queryBuilder.append(" AND lower(unit_title) LIKE '%' || lower(?) || '%'");
		if (filter.getAuthor() != null)
			queryBuilder.append(" AND author_id = ?");
		if (filter.getLecture() != null)
			queryBuilder.append(" AND EXISTS (SELECT 1 FROM lecture_units WHERE units.unit_id = lecture_units.unit_id AND lecture_units.lecture_id = ?)");
		if (count != null)
			queryBuilder.append(" LIMIT ?");
		if (offset != null)
			queryBuilder.append(" OFFSET ?");
	    
		try (PreparedStatement statement = connProvider.get().prepareStatement(queryBuilder.toString())) {
			int index = 0;
			if (filter.getType() != null)
			    statement.setString(++index, filter.getType().toString());
			if (filter.getTitle() != null)
			    statement.setString(++index, filter.getTitle());
			if (filter.getAuthor() != null)
			    statement.setObject(++index, filter.getAuthor().getId());
			if (filter.getLecture() != null)
			    statement.setObject(++index, filter.getLecture().getId());
			if (count != null)
				statement.setInt(++index, count);
			if (offset != null)
				statement.setInt(++index, offset);
	        try (ResultSet resultSet = statement.executeQuery()) {
	        	while(resultSet.next())
	        		units.add(fromResultSet(resultSet));
	        }
		} catch (SQLException e) {
			throw new RuntimeException("Unable to list units", e);
		}
		
	    return units;

	}

	/** Returns units count for given {@link Lecture}.*/
	public int count(Lecture lecture) {
		
		try (PreparedStatement statement = connProvider.get().prepareStatement("SELECT COUNT(*) FROM lecture_units WHERE lecture_id = ?")) {
		    statement.setObject(1, lecture.getId());
	        try (ResultSet resultSet = statement.executeQuery()) {
	        	if (!resultSet.next())
	        		throw new SQLException("Empty ResultSet?!");
	        	return resultSet.getInt(1);
	        }
		} catch (SQLException e) {
			throw new RuntimeException("Unable to count units for lecture " + lecture, e);
		}
		
	}

	/** Returns all units for given {@link Lecture} in proper order.*/
	public List<Unit> list(Lecture lecture) {
		
		List<Unit> units = new ArrayList<>();
		
		try (PreparedStatement statement = connProvider.get().prepareStatement(
			"SELECT units.* FROM units" +
			"	JOIN lecture_units ON lecture_units.unit_id = units.unit_id" +
			"	WHERE lecture_units.lecture_id = ?" +
			"	ORDER BY lecture_units.unit_ordinal ASC"
		)) {
		    statement.setObject(1, lecture.getId());
	        try (ResultSet resultSet = statement.executeQuery()) {
	        	while(resultSet.next())
	        		units.add(fromResultSet(resultSet));
	        }
		} catch (SQLException e) {
			throw new RuntimeException("Unable to list units for lecture " + lecture, e);
		}
		
	    return units;

	}

	/** Parses given ResultSet and extract Unit instance from it. */
	public Unit fromResultSet(ResultSet resultSet) {

		try {
			
			Unit unit = null;
			
			Unit.Type type = Unit.Type.valueOf(resultSet.getString("unit_type"));
			String attributesAsString = resultSet.getString("unit_attributes");
			switch (type) {
				case Text: {
					unit = new TextUnit();
					unit.setAttributes(mapper.readValue(attributesAsString, TextUnit.Attributes.class));
					break;
				}
				case Video:
					unit = new VideoUnit();
					unit.setAttributes(mapper.readValue(attributesAsString, VideoUnit.Attributes.class));
					break;
				case Audio:
					throw new NotImplementedException("Audio unit not implemented");
				case Image:
					throw new NotImplementedException("Image unit not implemented");
				case Quiz:
					throw new NotImplementedException("Quiz unit not implemented");
				case Series:
					throw new NotImplementedException("Series unit not implemented");
				case YouTube:
					throw new NotImplementedException("YouTube unit not implemented");
			}
	
			if (unit == null)
				throw new IllegalArgumentException("Unknown unit type: " + type);
	
			unit.setType(type);

			unit.setId(UUID.class.cast(resultSet.getObject("unit_id")));
			unit.setTitle(resultSet.getString("unit_title"));
			unit.setAuthor(userProvider.get().findById(UUID.class.cast(resultSet.getObject("author_id"))));
			unit.setKeywords(JdbcUtils.array2List(resultSet.getArray("unit_keywords"), String[].class));
			
			return unit;

		} catch (SQLException | IOException e) {
			throw new RuntimeException("Unable to resolve unit from result set", e);
		}

	}
	
	@SuppressWarnings("unused")
	private static final Logger Log = LoggerFactory.getLogger(UnitManager.class);

	private final Provider<UserManager> userProvider;
	private final Provider<Connection> connProvider;
	private final ObjectMapper mapper;
	
}
