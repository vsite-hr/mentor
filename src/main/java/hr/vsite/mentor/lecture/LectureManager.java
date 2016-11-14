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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hr.vsite.mentor.Mentor;
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
			throw new RuntimeException("Unable to find lecture with id " + id, e.getCause());
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
		queryBuilder.append(" AND EXISTS (SELECT 1 FROM course_lectures cl WHERE l.lecture_id = cl.lecture_id AND cl.course_id = ?)");
		if (filter.getTitle() != null)
		{
			queryBuilder.append(" AND lower(l.lecture_title COLLATE \"").
									append(Mentor.DefaultLocale.toString()).
									append("\") LIKE lower('%'||?||'%' COLLATE \"").
									append(Mentor.DefaultLocale.toString()).
									append("\")");
		} 
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
			throw new RuntimeException("Unable to list lectures", e.getCause());
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
		} 
		catch (SQLException e) {
			throw new RuntimeException("Unable to resolve lectures from result set", e.getCause());
		}
		
		return lecture;
	}
	
	private static final Logger Log = LoggerFactory.getLogger(LectureManager.class);

	private final Provider<UserManager> userProvider;
	private final Provider<Connection> connProvider;
	
}
