package hr.vsite.mentor.web;

import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;

import hr.vsite.mentor.web.places.MentorPlaceHistoryMapper;
import hr.vsite.mentor.web.places.ClassroomPlace;

public class Places {

	public static PlaceController controller() { return placeController; }
	public static MentorPlaceHistoryMapper mapper() { return historyMapper; }
	public static PlaceHistoryHandler handler() { return historyHandler; }

	public static void goHome() {
		placeController.goTo(new ClassroomPlace());
	}
	
	private static final PlaceController placeController;
	private static final MentorPlaceHistoryMapper historyMapper;
	private static final PlaceHistoryHandler historyHandler;

	static {

		placeController = new PlaceController(MentorBus.get());
		
		historyMapper = GWT.create(MentorPlaceHistoryMapper.class);
        
		historyHandler = new PlaceHistoryHandler(historyMapper);
		historyHandler.register(placeController, MentorBus.get(), new ClassroomPlace());

	}
	
}
