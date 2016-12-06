package hr.vsite.mentor.web.activities;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;

import hr.vsite.mentor.web.places.CoursePlace;
import hr.vsite.mentor.web.places.LecturePlace;
import hr.vsite.mentor.web.places.WelcomePlace;

public class MentorActivityMapper implements ActivityMapper {

	@Override
	public Activity getActivity(Place place) {
		
		if (place instanceof WelcomePlace)
			return new WelcomeActivity((WelcomePlace) place);
		else if (place instanceof CoursePlace)
			return new CourseActivity((CoursePlace) place);
		else if (place instanceof LecturePlace)
			return new LectureActivity((LecturePlace) place);

		return null;
		
	}

}
