package hr.vsite.mentor.lecture;

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

public class LectureManager {

	@Inject
	public LectureManager(Provider<UserManager> userProvider, Provider<Connection> connProvider) {
		this.userProvider = userProvider;
		this.connProvider = connProvider;
	}

	/** Returns <code>Lecture</code> with corresponding id, or <code>null</code> if such lecture does not exist. */
	public Lecture findById(UUID id) {

		// TODO implement LectureManager.findById(UUID)
		
		throw new NotImplementedException("LectureManager.findById(UUID)");
		
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
		
		// TODO implement LectureManager.list(LectureFilter, Integer, Integer)
		
		throw new NotImplementedException("LectureManager.list(LectureFilter, Integer, Integer)");
		
	}
	
	/** Parses given ResultSet and extract {@link Course} from it.
	 * If ResultSet had <code>NULL</code> in <code>course_id</code> column, <code>null</code> is returned. */
	public Lecture lectureFromResultSet(ResultSet resultSet) {
		
		Lecture lecture = new Lecture();
		
		try {
			lecture.setId(UUID.class.cast(resultSet.getObject("course_id")));
			if (resultSet.wasNull())
				return null;
			lecture.setTitle(resultSet.getString("course_title"));
			lecture.setDescription(resultSet.getString("course_description"));
			lecture.setAuthor(userProvider.get().findById(UUID.class.cast(resultSet.getObject("author_id"))));
		} catch (SQLException e) {
			throw new RuntimeException("Unable to resolve course from result set", e);
		}
		
		return lecture;
		
	}
	
	private static final Logger Log = LoggerFactory.getLogger(LectureManager.class);

	private final Provider<UserManager> userProvider;
	private final Provider<Connection> connProvider;
	
}
