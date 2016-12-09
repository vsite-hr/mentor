package hr.vsite.mentor.web.places;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class ClassroomPlace extends Place {

	public static final String Token = "!classroom";
	
	@Prefix(Token)
	public static class Tokenizer implements PlaceTokenizer<ClassroomPlace> {
		@Override
		public String getToken(ClassroomPlace place) {
			return "";
		}
		@Override
		public ClassroomPlace getPlace(String token) {
			return new ClassroomPlace();
		}
	}

}
