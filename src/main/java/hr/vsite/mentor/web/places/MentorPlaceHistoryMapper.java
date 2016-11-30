package hr.vsite.mentor.web.places;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;

@WithTokenizers({
	WelcomePlace.Tokenizer.class,
	CoursePlace.Tokenizer.class
})
public interface MentorPlaceHistoryMapper extends PlaceHistoryMapper {

}
