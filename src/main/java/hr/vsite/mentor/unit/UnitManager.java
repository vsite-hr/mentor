package hr.vsite.mentor.unit;

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

import hr.vsite.mentor.user.UserManager;

public class UnitManager {

	@Inject
	public UnitManager(Provider<UserManager> userProvider, Provider<Connection> connProvider) {
		this.userProvider = userProvider;
		this.connProvider = connProvider;
	}

	/** Returns <code>Unit</code> with corresponding id, or <code>null</code> if such unit does not exist. */
	public Unit findById(UUID id) {
		
	    try (PreparedStatement statement = connProvider.get().prepareStatement("SELECT unit_type FROM units WHERE unit_id = ?")) {
		    statement.setObject(1, id);
	        try (ResultSet resultSet = statement.executeQuery()) {
	        	return resultSet.next() ? findById(id, UnitType.valueOf(resultSet.getString("unit_type"))) : null;
	        }
		} catch (SQLException e) {
			throw new RuntimeException("Unable to find unit with id " + id, e);
		}
	    
	}
	
	/** Returns <code>Unit</code> with corresponding id that is of known type, or <code>null</code> if such unit does not exist. */
	public Unit findById(UUID id, UnitType type) {
		
		switch (type) {
			case Text: return findTextById(id);
			case Video: throw new NotImplementedException("Video unit not implemented");
			case Audio: throw new NotImplementedException("Audio unit not implemented");
			case Image: throw new NotImplementedException("Image unit not implemented");
			case Quiz: throw new NotImplementedException("Quiz unit not implemented");
		}

		throw new IllegalArgumentException("Unknown unit type: " + type);

	}
	
	/** Returns text <code>Unit</code> with corresponding id, or <code>null</code> if such text unit does not exist. */
	public TextUnit findTextById(UUID id) {
		
	    try (PreparedStatement statement = connProvider.get().prepareStatement("SELECT * FROM units_text WHERE unit_id = ?")) {
		    statement.setObject(1, id);
	        try (ResultSet resultSet = statement.executeQuery()) {
	        	return resultSet.next() ? textFromResultSet(resultSet) : null;
	        }
		} catch (SQLException e) {
			throw new RuntimeException("Unable to find text unit with id " + id, e);
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
			queryBuilder.append(" AND lecture_id = ?");
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
	        		units.add(findById(UUID.class.cast(resultSet.getObject("unit_id")), UnitType.valueOf(resultSet.getString("unit_type"))));
	        }
		} catch (SQLException e) {
			throw new RuntimeException("Unable to list users", e);
		}
		
	    return units;

	}

	/** Parses given ResultSet and extract text Unit instance from it.
	 * If ResultSet had <code>NULL</code> in <code>unit_id</code> column, <code>null</code> is returned. */
	public TextUnit textFromResultSet(ResultSet resultSet) {

		TextUnit unit = new TextUnit();
		
		if (!baseFromResultSet(unit, resultSet))
			return null;
		
		try {
			unit.setMarkupType(MarkupType.valueOf(resultSet.getString("unit_markup_type")));
			unit.setMarkup(resultSet.getString("unit_markup"));
		} catch (SQLException e) {
			throw new RuntimeException("Unable to resolve text unit from result set", e);
		}
		
		return unit;
		
	}
	
	/** Parses given ResultSet and extract Unit base properties from it. */
	private boolean baseFromResultSet(Unit unit, ResultSet resultSet) {

		try {
			unit.setId(UUID.class.cast(resultSet.getObject("unit_id")));
			if (resultSet.wasNull())
				return false;
			unit.setType(UnitType.valueOf(resultSet.getString("unit_type")));
			unit.setTitle(resultSet.getString("unit_title"));
			unit.setAuthor(userProvider.get().findById(UUID.class.cast(resultSet.getObject("author_id"))));
		} catch (SQLException e) {
			throw new RuntimeException("Unable to resolve base unit from result set", e);
		}
		
		return true;
		
	}
	
	private static final Logger Log = LoggerFactory.getLogger(UnitManager.class);

	private final Provider<UserManager> userProvider;
	private final Provider<Connection> connProvider;
	
}
