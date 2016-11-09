package hr.vsite.mentor.course;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Provider;

import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hr.vsite.mentor.user.UserManager;

public class CourseManager {

	@Inject
	public CourseManager(Provider<UserManager> userProvider, Provider<Connection> connProvider) {
		this.userProvider = userProvider;
		this.connProvider = connProvider;
	}

	/** Returns <code>Course</code> with corresponding id, or <code>null</code> if such course does not exist. */
	public Course findById(UUID id) {

		// TODO implement CourseManager.findById(UUID)
		
		throw new NotImplementedException("CourseManager.findById(UUID)");
		
	}
	
	/** Returns all courses known to application, unpaged. If there is a possibility for large number of courses,
	 * use {@link #list(CourseFilter, Integer, Integer)} */
	public List<Course> list() {
		return list(new CourseFilter(), null, null);
	}
	
	/** Returns courses that match given criteria, unpaged. If there is a possibility for large number of courses,
	 * use {@link #list(CourseFilter, Integer, Integer)} */
	public List<Course> list(CourseFilter filter) {
		return list(filter, null, null);
	}
	
	/** Returns courses that match given criteria, paged. */
	public List<Course> list(CourseFilter filter, Integer count, Integer offset) {

		// TODO implement CourseManager.list(CourseFilter, Integer, Integer)
		
		throw new NotImplementedException("CourseManager.list(CourseFilter, Integer, Integer)");
		
	}
	
	/** Parses given ResultSet and extract {@link Course} from it.
	 * If ResultSet had <code>NULL</code> in <code>course_id</code> column, <code>null</code> is returned. */
	public Course fromResultSet(ResultSet resultSet) {
		
		Course course = new Course();
		
		try {
			course.setId(UUID.class.cast(resultSet.getObject("course_id")));
			if (resultSet.wasNull())
				return null;
			course.setTitle(resultSet.getString("course_title"));
			course.setDescription(resultSet.getString("course_description"));
			course.setAuthor(userProvider.get().findById(UUID.class.cast(resultSet.getObject("author_id"))));
		} catch (SQLException e) {
			throw new RuntimeException("Unable to resolve course from result set", e);
		}
		
		return course;
		
	}
	
	private static final Logger Log = LoggerFactory.getLogger(CourseManager.class);

	private final Provider<UserManager> userProvider;
	private final Provider<Connection> connProvider;
	
}
