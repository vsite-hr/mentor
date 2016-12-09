package hr.vsite.mentor.web.places;

import java.util.Map;
import java.util.UUID;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import hr.vsite.mentor.web.places.tokens.TokenBuilder;
import hr.vsite.mentor.web.places.tokens.TokenParser;

public class ClassroomPlace extends Place {

	public static final String Token = "!classroom";
	
	public static class Filter {

		public static final String Lecturer	= "lecturer";
		
		public static Filter parse(Map<String, String> filters) {
			Filter filter = new Filter();
			if (filters.containsKey(Lecturer))
				filter.setLecturerId(UUID.fromString(filters.get(Lecturer)));
			return filter;
		}
		
		public UUID getLecturerId() { return lecturerId; }
		public Filter setLecturerId(UUID lecturerId) { this.lecturerId = lecturerId; return this; }
		
		public void build(TokenBuilder builder) {
			builder.createFilter();
			if (lecturerId != null)
				builder.appendFilter(Lecturer, lecturerId.toString());
		}
		
		private UUID lecturerId;
		
	}
	
	@Prefix(Token)
	public static class Tokenizer implements PlaceTokenizer<ClassroomPlace> {
		
		@Override
		public String getToken(ClassroomPlace place) {
			TokenBuilder builder = new TokenBuilder();
			if (place.getFilter() != null)
				place.getFilter().build(builder);
			return builder.toString();
		}
		
		@Override
		public ClassroomPlace getPlace(String token) {
			TokenParser parser = new TokenParser(token);
			try {
				ClassroomPlace place = new ClassroomPlace(); 
				if (parser.getFilters() != null)
					place.setFilter(Filter.parse(parser.getFilters()));
				return place;
			} catch (Exception e) {
				return new ClassroomPlace();
			}
		}

	}

	public ClassroomPlace() {
		this(null);
	}
	
	public ClassroomPlace(Filter filter) {
		this.filter = filter;
	}
	
	public Filter getFilter() { return filter; }
	public ClassroomPlace setFilter(Filter filter) { this.filter = filter; return this; }
	public Filter filter() { return filter = new Filter(); }

	private Filter filter = null;
	
}
