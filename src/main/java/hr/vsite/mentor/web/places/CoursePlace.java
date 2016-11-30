package hr.vsite.mentor.web.places;

import java.util.UUID;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import hr.vsite.mentor.web.places.tokens.TokenBuilder;
import hr.vsite.mentor.web.places.tokens.TokenParser;

public class CoursePlace extends Place {

	public static final String Token = "!course";
	
	@Prefix(Token)
	public static class Tokenizer implements PlaceTokenizer<CoursePlace> {
		@Override
		public String getToken(CoursePlace place) {
			TokenBuilder builder = new TokenBuilder(place.getCourseId().toString());
			return builder.toString();
		}
		@Override
		public CoursePlace getPlace(String token) {
			TokenParser parser = new TokenParser(token);
			try {
				String tokens[] = parser.getPrimaryTokens();
				String courseId = tokens[0];
				return new CoursePlace(UUID.fromString(courseId));
			} catch (Exception e) {
				// CourseActivity will check for null values
				return new CoursePlace(null);
			}
		}
	}

	public CoursePlace(UUID courseId) {
		this.courseId = courseId;
	}
	
	public UUID getCourseId() { return courseId; }

	private final UUID courseId;

}
