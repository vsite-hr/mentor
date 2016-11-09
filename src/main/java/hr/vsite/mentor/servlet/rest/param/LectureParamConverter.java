package hr.vsite.mentor.servlet.rest.param;

import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.transaction.Transactional;
import javax.ws.rs.ext.ParamConverter;

import org.apache.commons.lang3.StringUtils;

import hr.vsite.mentor.lecture.Lecture;
import hr.vsite.mentor.lecture.LectureManager;

public class LectureParamConverter implements ParamConverter<Lecture> {

	@Inject
	LectureParamConverter(Provider<LectureManager> lectureProvider) {
		this.lectureProvider = lectureProvider;
	}
	
	@Override
	@Transactional
	public Lecture fromString(String id) {

		if (StringUtils.isBlank(id))
			return null;
		
		return lectureProvider.get().findById(UUID.fromString(id));
		
	}

	@Override
	public String toString(Lecture lecture) {
		if (lecture == null)
			return null;
		return lecture.getId().toString();
	}

	private final Provider<LectureManager> lectureProvider;
	
}
