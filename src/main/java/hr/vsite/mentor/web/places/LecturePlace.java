package hr.vsite.mentor.web.places;

import java.util.UUID;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import hr.vsite.mentor.web.places.tokens.TokenBuilder;
import hr.vsite.mentor.web.places.tokens.TokenParser;

public class LecturePlace extends Place {

	public static final String Token = "!lecture";
	
	@Prefix(Token)
	public static class Tokenizer implements PlaceTokenizer<LecturePlace> {
		@Override
		public String getToken(LecturePlace place) {
			TokenBuilder builder = new TokenBuilder(place.getCourseId().toString(), place.getLectureId().toString());
			return builder.toString();
		}
		@Override
		public LecturePlace getPlace(String token) {
			TokenParser parser = new TokenParser(token);
			try {
				String tokens[] = parser.getPrimaryTokens();
				String courseId = tokens[0];
				String lectureId = tokens[1];
				return new LecturePlace(UUID.fromString(courseId), UUID.fromString(lectureId));
			} catch (Exception e) {
				// LectureActivity will check for null values
				return new LecturePlace(null, null);
			}
		}
	}

	public LecturePlace(UUID courseId, UUID lectureId) {
		this.courseId = courseId;
		this.lectureId = lectureId;
	}
	
	public UUID getCourseId() { return courseId; }
	public UUID getLectureId() { return lectureId; }

	private final UUID courseId;
	private final UUID lectureId;

}
