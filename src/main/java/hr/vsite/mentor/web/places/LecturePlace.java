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
			TokenBuilder builder = new TokenBuilder(place.getLectureId().toString());
			return builder.toString();
		}
		@Override
		public LecturePlace getPlace(String token) {
			TokenParser parser = new TokenParser(token);
			try {
				String tokens[] = parser.getPrimaryTokens();
				String lectureId = tokens[0];
				return new LecturePlace(UUID.fromString(lectureId));
			} catch (Exception e) {
				// LectureActivity will check for null values
				return new LecturePlace(null);
			}
		}
	}

	public LecturePlace(UUID lectureId) {
		this.lectureId = lectureId;
	}
	
	public UUID getLectureId() { return lectureId; }

	private final UUID lectureId;

}
