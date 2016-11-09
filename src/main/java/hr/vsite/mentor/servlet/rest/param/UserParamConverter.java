package hr.vsite.mentor.servlet.rest.param;

import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.transaction.Transactional;
import javax.ws.rs.ext.ParamConverter;

import org.apache.commons.lang3.StringUtils;

import hr.vsite.mentor.user.User;
import hr.vsite.mentor.user.UserManager;

public class UserParamConverter implements ParamConverter<User> {

	@Inject
	UserParamConverter(Provider<UserManager> userProvider) {
		this.userProvider = userProvider;
	}
	
	@Override
	@Transactional
	public User fromString(String id) {

		if (StringUtils.isBlank(id))
			return null;
		
		return userProvider.get().findById(UUID.fromString(id));
		
	}

	@Override
	public String toString(User user) {
		if (user == null)
			return null;
		return user.getId().toString();
	}

	private final Provider<UserManager> userProvider;
	
}
