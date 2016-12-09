package hr.vsite.mentor.web.places;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class LecturersPlace extends Place {

	public static final String Token = "!lecturers";
	
	@Prefix(Token)
	public static class Tokenizer implements PlaceTokenizer<LecturersPlace> {
		@Override
		public String getToken(LecturersPlace place) {
			return "";
		}
		@Override
		public LecturersPlace getPlace(String token) {
			return new LecturersPlace();
		}
	}

}
