package hr.vsite.mentor.lecture;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.InternalServerErrorException;

//import gwt.material.design.jscore.client.api.Array;
import hr.vsite.mentor.Mentor;
import hr.vsite.mentor.course.Course;
import hr.vsite.mentor.db.JdbcUtils;
import hr.vsite.mentor.unit.Unit;
import hr.vsite.mentor.unit.UnitManager;
import hr.vsite.mentor.user.UserManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LectureManager {

	@Inject
	public LectureManager(Provider<UserManager> userProvider, Provider<Connection> connProvider, Provider<UnitManager> unitProvider) {
		this.userProvider = userProvider;
		this.unitProvider = unitProvider;
		this.connProvider = connProvider;
	}

	/** Returns <code>Lecture</code> with corresponding id, or <code>null</code> if such lecture does not exist. */
	public Lecture findById(UUID id) {

		try (PreparedStatement statement = connProvider.get().prepareStatement("SELECT * FROM lectures WHERE lecture_id = ?")) {
		    statement.setObject(1, id);
	        try (ResultSet resultSet = statement.executeQuery()) {
	        	return resultSet.next() ? lectureFromResultSet(resultSet) : null;
	        }
		} 
		catch (SQLException e) {
			throw new RuntimeException("Unable to find lecture with id " + id, e);
		}	
	}
	
	/** Returns all lectures known to application, unpaged. If there is a possibility for large number of lectures,
	 * use {@link #list(LectureFilter, Integer, Integer)} */
	public List<Lecture> list() {
		return list(new LectureFilter(), null, null);
	}
	
	/** Returns lectures that match given criteria, unpaged. If there is a possibility for large number of lectures,
	 * use {@link #list(LectureFilter, Integer, Integer)} */
	public List<Lecture> list(LectureFilter filter) {
		return list(filter, null, null);
	}
	
	/** Returns lectures that match given criteria, paged. */
	public List<Lecture> list(LectureFilter filter, Integer count, Integer offset) {
		
		List<Lecture> lectures = new ArrayList<>(count != null ? count : 10);
		
		StringBuilder queryBuilder = new StringBuilder(1000);
		queryBuilder.append("SELECT l.* FROM lectures l WHERE true"); 
		if(filter.getCourse() != null)
			queryBuilder.append(" AND EXISTS (SELECT 1 FROM course_lectures cl WHERE l.lecture_id = cl.lecture_id AND cl.course_id = ?)");
		if (filter.getTitle() != null)
			queryBuilder.append(" AND lower(l.lecture_title COLLATE \""+
										Mentor.DefaultLocale.toString()+
										"\") LIKE lower('%'||?||'%' COLLATE \""+
										Mentor.DefaultLocale.toString()+"\")"); 
		if (filter.getAuthor() != null)
			queryBuilder.append(" AND l.author_id = ?");
		if (count != null)
			queryBuilder.append("LIMIT ?");
		if (offset != null)
			queryBuilder.append("OFFSET ?");
	    
		try (PreparedStatement statement = connProvider.get().prepareStatement(queryBuilder.toString())) {
			int index = 0;
			if (filter.getCourse() != null)
			    statement.setObject(++index, filter.getCourse().getId());
			if (filter.getTitle() != null)
			    statement.setString(++index, filter.getTitle());
			if (filter.getAuthor() != null)
			    statement.setObject(++index, filter.getAuthor().getId());
			if (count != null)
				statement.setInt(++index, count);
			if (offset != null)
				statement.setInt(++index, offset);
			
	        try (ResultSet resultSet = statement.executeQuery()) {
	        	while(resultSet.next())
	        		lectures.add(lectureFromResultSet(resultSet));
	        }
		} 
		catch (SQLException e) {
			throw new RuntimeException("Unable to list lectures", e);
		}
		
	    return lectures;	
	}
	
	/** Returns all lectures for given {@link Course} in proper order.*/
	public List<Lecture> list(Course course){
		
		List<Lecture> lectures = new ArrayList<Lecture>();
		
		String query = "SELECT lectures.* FROM lectures "
						+ "JOIN course_lectures ON (lectures.lecture_id = course_lectures.lecture_id) "
						+ "WHERE course_lectures.course_id = ? "
						+ "ORDER BY course_lectures.lecture_ordinal ASC";
		
		try (PreparedStatement statement = connProvider.get().prepareStatement(query)){
			statement.setObject(1, course.getId());
			try(ResultSet resultSet = statement.executeQuery()){
				while(resultSet.next())
					lectures.add(lectureFromResultSet(resultSet));			
			}
		}
		catch(SQLException e){
			throw new RuntimeException("Unable to resolve lectures for this Course: " + course.getId(), e);
		}
		
		return lectures;
	}
	
	/** Returns lecture count for given {@link Course}.*/
	public int count(Course course){
		
		String query = "SELECT COUNT(*) AS lecture_num FROM course_lectures where course_id = ?";
		
		try (PreparedStatement statement = connProvider.get().prepareStatement(query)){
			statement.setObject(1, course.getId());
			try(ResultSet resultSet = statement.executeQuery()){
				if (!resultSet.next())
					throw new SQLException("There is no lectures assigned to this Course: " + course.getId());
			
				return resultSet.getInt("lecture_num");
			}
		}
		catch(SQLException e){
			throw new RuntimeException("Unable to resolve lectures for this Course: " + course.getId(), e);
		}
	}
	
	/** Returns lecture_head_unit for given {@link Course}.*/
	public Unit getHeadUnit(UUID lectureId){
		
		Unit headUnit = null;
		
		String query = "SELECT u.* FROM units u JOIN lectures l ON (u.unit_id = l.lecture_head_unit_id) AND l.lecture_id = ?";
		
		try(PreparedStatement statement = connProvider.get().prepareStatement(query)){
			statement.setObject(1, lectureId);			
			try(ResultSet resultSet = statement.executeQuery()){
				if(resultSet.next())
					headUnit = unitProvider.get().fromResultSet(resultSet);			
			}
		}
		catch(SQLException e){
			throw new RuntimeException("Unable to resolve lectureHeadUnit: " + e.getMessage());
		}
		
		return headUnit;
	}
	
	/** Parses given ResultSet and extract {@link Lectures} from it.
	 * If ResultSet had <code>NULL</code> in <code>Lectures_id</code> column, <code>null</code> is returned. */
	public Lecture lectureFromResultSet(ResultSet resultSet) {
		
		Lecture lecture = new Lecture();
		
		try {
			lecture.setId(UUID.class.cast(resultSet.getObject("lecture_id")));
			if (resultSet.wasNull())
				return null;
			lecture.setTitle(resultSet.getString("lecture_title"));
			lecture.setDescription(resultSet.getString("lecture_description"));
			lecture.setAuthor(userProvider.get().findById(UUID.class.cast(resultSet.getObject("author_id"))));
			lecture.setKeywords(JdbcUtils.array2List(resultSet.getArray("lecture_keywords"), String[].class));
		} 
		catch (SQLException e) {
			throw new RuntimeException("Unable to resolve lectures from result set", e);
		}
		
		return lecture;
	}
	
	/**Insert new {@link Lecture} into database.*/
	public Lecture insert(Lecture lecture){
		
		String query = "INSERT INTO lectures (lecture_title, lecture_description, author_id, lecture_keywords) VALUES (?, ?, ?, ?)";	
		
		try (PreparedStatement statement = connProvider.get().prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)){
			int index = 0;
			statement.setString(++index, lecture.getTitle());
			statement.setString(++index, lecture.getDescription());
			statement.setObject(++index, lecture.getAuthor().getId());	
			statement.setArray(++index, connProvider.get().createArrayOf("text", lecture.getKeywords().toArray()));
			if(statement.executeUpdate() == 0)
				throw new SQLException("Something went wrong during insert operation");
			try(ResultSet result = statement.getGeneratedKeys()){
				lecture.setId(UUID.class.cast(result.getObject("lectur_id")));
			}
		}			
		catch (SQLException e) {
			throw new RuntimeException("Unable to insert Lecture" + e.getMessage());
		}
		
		return lecture;
	}
	
	/**Update {@link Lecture} in database for given ID*/
	public Lecture update(UUID lectureBeforeId, Lecture lectureAfter){
		
		String query = "UPDATE lectures SET lecture_title = ?, lecture_description = ?, author_id = ?, lecture_keywords = ? WHERE lecture_id = ?";
		
		try (PreparedStatement statement = connProvider.get().prepareStatement(query)) {
			
			int index = 0;
			statement.setString(++index, lectureAfter.getTitle());
			statement.setString(++index, lectureAfter.getDescription());
			statement.setObject(++index, lectureAfter.getAuthor().getId());
			statement.setArray(++index, connProvider.get().createArrayOf("text", lectureAfter.getKeywords().toArray()));
			statement.setObject(++index, lectureBeforeId);
			
			if(statement.executeUpdate() == 0)
				throw new SQLException("Something went wrong during insert operation");
		}
		catch (SQLException e) {
			throw new RuntimeException("Unable to update lecture: " + e.getMessage());
		}

		return lectureAfter;
	}
	
	/**Delete {@link Lecture} from database and all FK relations for given ID}
	 * @throws SQLException */
	@SuppressWarnings("resource")
	public Lecture delete(Lecture lecture){
				
		String query = "SELECT course_id FROM course_lectures WHERE lecture_id = ?";
			
		PreparedStatement statement = null;
		try {
			statement = connProvider.get().prepareStatement(query);
			statement.setObject(1, lecture.getId());
			ResultSet resultSet = statement.executeQuery();
			if(resultSet.next())
				throw new SQLException(
							"Unable to delete Lecture due to foreign key constraint on Course: [" 
							+resultSet.getObject("course_id")
							+"] in table course_lectures");
			
			statement = connProvider.get().prepareStatement("DELETE FROM lecture_units WHERE lecture_id = ?");
			statement.setObject(1, lecture.getId());
			statement.executeUpdate();
			statement = connProvider.get().prepareStatement("DELETE FROM lectures WHERE lecture_id = ?");
			statement.setObject(1, lecture.getId());
			if(statement.executeUpdate() == 0)
				throw new SQLException("Something went wrong during delete operation");
		} 
		catch (SQLException e) {
			throw new RuntimeException("Delete failed: " + e.getMessage());
		}
		finally{
			if(statement != null)
				try {
					statement.close();
				} catch (SQLException e) {
					throw new RuntimeException("Delete failed: " + e.getMessage());
				}
		}

		return lecture;
	}
	
	/** Returns all lectures, in proper order, that given {@link Unit} is part of.*/
	public List<Lecture> list(Unit unit){
		
		List<Lecture> lectures = new ArrayList<Lecture>();
		String query = "SELECT * FROM lectures JOIN lecture_units ON (lectures.lecture_id = lecture_units.lecture_id) WHERE lecture_units.unit_id = ? ORDER BY lecture_units.unit_ordinal ASC";
		try (PreparedStatement statement = connProvider.get().prepareStatement(query)){
			statement.setObject(1, unit.getId());
			try(ResultSet resultSet = statement.executeQuery()){
				while(resultSet.next())
					lectures.add(lectureFromResultSet(resultSet));			
			}
		}
		catch(SQLException e){
			throw new InternalServerErrorException("Unable to list lectures for unit " + unit.toString(), e);
		}
		Log.debug("User {} listed lectures that unit {} is part of", userProvider.get().me(), unit.toString());
		return lectures;
	}
	
	private static final Logger Log = LoggerFactory.getLogger(LectureManager.class);

	private final Provider<UserManager> userProvider;
	private final Provider<UnitManager> unitProvider;
	private final Provider<Connection> connProvider;
	
}
