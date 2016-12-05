package hr.vsite.mentor.lecture;

import java.sql.Array;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

//import gwt.material.design.jscore.client.api.Array;
import hr.vsite.mentor.Mentor;
import hr.vsite.mentor.db.JdbcUtils;
import hr.vsite.mentor.user.UserManager;

public class LectureManager {

	@Inject
	public LectureManager(Provider<UserManager> userProvider, Provider<Connection> connProvider) {
		this.userProvider = userProvider;
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
			lecture.setLectureKeywords(JdbcUtils.array2List(resultSet.getArray("lecture_keywords"), String[].class));
		} 
		catch (SQLException e) {
			throw new RuntimeException("Unable to resolve lectures from result set", e);
		}
		
		return lecture;
	}
	
	public Lecture insert(LectureFilter filter, Lecture lecture){
		
		String query = "INSERT INTO lectures (lecture_title, lecture_description, author_id, lecture_keywords) VALUES (?, ?, ?, ?)";	
		
		try {
			PreparedStatement statement = connProvider.get().prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
			int index = 0;
			statement.setString(++index, lecture.getTitle());
			statement.setString(++index, lecture.getDescription());
			statement.setObject(++index, lecture.getAuthor().getId());	
			statement.setArray(++index, connProvider.get().createArrayOf("text", lecture.getLectureKeywords().toArray()));
			if(statement.executeUpdate() == 0)
				throw new SQLException("Something went wrong during insert operation");
			ResultSet resultSet = statement.getGeneratedKeys();

			if(resultSet.next()){
				lecture.setId(UUID.class.cast(resultSet.getObject("lecture_id")));
				
				// Creating relation Lecture -> Course
				if(filter.getCourse() != null){
				index = 0;
				query = "INSERT INTO course_lectures (course_id, lecture_id, lecture_ordinal) VALUES (?, ?, (SELECT COUNT(*) FROM course_lectures WHERE course_id = ?) + 1)";
				statement = connProvider.get().prepareStatement(query);
				statement.setObject(++index, filter.getCourse().getId());
				statement.setObject(++index, lecture.getId());
				statement.setObject(++index, filter.getCourse().getId());
				if(statement.executeUpdate() == 0)
					throw new SQLException("Unable to create relation: Lecture -> Course");
				}
			}
			else
				throw new SQLException("getGeneratedKeys(), Did not return auto-generated key for last inserted Lecture.");
		} 
		catch (SQLException e) {
			Log.info("Unable to insert Lecture: " + e.getMessage());
			throw new RuntimeException("Unable to insert Lecture" + e.getMessage());
		}
		
		return lecture;
	}
	
	public Lecture update(Lecture lectureBefore, Lecture lectureAfter){
		
		String query = "UPDATE lectures SET lecture_title = ?, lecture_description = ?, author_id = ?, lecture_keywords = ? WHERE lecture_id = ?";
		
		try (PreparedStatement statement = connProvider.get().prepareStatement(query)) {
			
			int index = 0;
			statement.setString(++index, lectureAfter.getTitle());
			statement.setString(++index, lectureAfter.getDescription());
			statement.setObject(++index, lectureAfter.getAuthor().getId());
			statement.setArray(++index, connProvider.get().createArrayOf("text", lectureAfter.getLectureKeywords().toArray()));
			statement.setObject(++index, lectureBefore.getId());
			
			if(statement.executeUpdate() == 0)
				throw new SQLException("Something went wrong during insert operation");
		}
		catch (SQLException e) {
			Log.info("Unable to update Lecture: " + e.getMessage());
			throw new RuntimeException("Unable to update lecture: " + e.getMessage());
		}

		lectureAfter.setId(lectureBefore.getId());
		return lectureAfter;
	}
	
	public Lecture delete(Lecture lecture){
				
		String query = "SELECT course_id FROM course_lectures WHERE lecture_id = ?";
			
		try {
			PreparedStatement statement = connProvider.get().prepareStatement(query);
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
			Log.info("Delete failed: " + e.getMessage());
			throw new RuntimeException("Delete failed: " + e.getMessage());
		}

		return lecture;
	}
	
	private static final Logger Log = LoggerFactory.getLogger(LectureManager.class);

	private final Provider<UserManager> userProvider;
	private final Provider<Connection> connProvider;
	
}
