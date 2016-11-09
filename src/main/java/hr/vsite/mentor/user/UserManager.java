package hr.vsite.mentor.user;

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

public class UserManager {

	@Inject
	public UserManager(Provider<Connection> connProvider) {
		this.connProvider = connProvider;
	}

	/** Returns <code>User</code> with corresponding id, or <code>null</code> if such user does not exist. */
	public User findById(UUID id) {
		
	    try (PreparedStatement statement = connProvider.get().prepareStatement("SELECT * FROM users WHERE user_id = ?")) {
		    statement.setObject(1, id);
	        try (ResultSet resultSet = statement.executeQuery()) {
	        	return resultSet.next() ? fromResultSet(resultSet) : null;
	        }
		} catch (SQLException e) {
			throw new RuntimeException("Unable to find user with id " + id, e);
		}
	    
	}
	
	/** Returns <code>User</code> with corresponding e-mail, or <code>null</code> if such user does not exist. */
	public User findByEmail(String email) {
		
	    try (PreparedStatement statement = connProvider.get().prepareStatement("SELECT * FROM users WHERE email = lower(?)")) {
		    statement.setString(1, email);
	        try (ResultSet resultSet = statement.executeQuery()) {
	        	return resultSet.next() ? fromResultSet(resultSet) : null;
	        }
		} catch (SQLException e) {
			throw new RuntimeException("Unable to find user with email " + email, e);
		}
	    
	}
	
	/** Returns all users known to application, unpaged. If there is a possibility for large number of users,
	 * use {@link #list(UserFilter, Integer, Integer)}*/
	public List<User> list() {
		return list(new UserFilter(), null, null);
	}
	
	/** Returns users that match given criteria, unpaged. If there is a possibility for large number of users,
	 * use {@link #list(UserFilter, Integer, Integer)}*/
	public List<User> list(UserFilter filter) {
		return list(filter, null, null);
	}
	
	/** Returns users that match given criteria, paged.*/
	public List<User> list(UserFilter filter, Integer count, Integer offset) {
		
		List<User> users = new ArrayList<>(count != null ? count : 10);
		
		StringBuilder queryBuilder = new StringBuilder(1000);
		queryBuilder.append("SELECT * FROM users WHERE true");
		if (filter.getName() != null)
			queryBuilder.append(" AND lower(user_name) LIKE '%' || lower(?) || '%'");
		if (filter.getEmail() != null)
			queryBuilder.append(" AND user_email = lower(?)");
		if (count != null)
			queryBuilder.append(" LIMIT ?");
		if (offset != null)
			queryBuilder.append(" OFFSET ?");
	    
		try (PreparedStatement statement = connProvider.get().prepareStatement(queryBuilder.toString())) {
			int index = 0;
			if (filter.getName() != null)
			    statement.setString(++index, filter.getName());
			if (filter.getEmail() != null)
			    statement.setString(++index, filter.getEmail());
			if (count != null)
				statement.setInt(++index, count);
			if (offset != null)
				statement.setInt(++index, offset);
	        try (ResultSet resultSet = statement.executeQuery()) {
	        	while(resultSet.next())
	        		users.add(fromResultSet(resultSet));
	        }
		} catch (SQLException e) {
			throw new RuntimeException("Unable to list users", e);
		}
		
	    return users;

	}

	/** Parses given ResulktSet and extract User from it.
	 * If ResultSet had <code>NULL</code> in <code>author_id</code> column, <code>null</code> is returned. */
	public User fromResultSet(ResultSet resultSet) {
		
		User user = new User();
		
		try {
			user.setId(UUID.class.cast(resultSet.getObject("user_id")));
			if (resultSet.wasNull())
				return null;
			user.setEmail(resultSet.getString("user_email"));
			user.setName(resultSet.getString("user_name"));
		} catch (SQLException e) {
			throw new RuntimeException("Unable to resolve user from result set", e);
		}
		
		return user;
		
	}
	
	private static final Logger Log = LoggerFactory.getLogger(UserManager.class);

	private final Provider<Connection> connProvider;
	
}
