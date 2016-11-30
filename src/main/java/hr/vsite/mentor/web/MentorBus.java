package hr.vsite.mentor.web;

import com.google.web.bindery.event.shared.Event;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class MentorBus {

	public static EventBus get() {
		if (instance == null)
			instance = new SimpleEventBus();
		return instance;
	}

	public static <H> HandlerRegistration register(Event.Type<H> type, H handler) {
		return get().addHandler(type, handler);
	}
	
	public static <H> HandlerRegistration register(Event.Type<H> type, java.lang.Object source, H handler) {
		return get().addHandlerToSource(type, source, handler);
	}
	
	public static void fire(Event<?> event) {
		get().fireEvent(event);
	}
	
	public static void fire(Event<?> event, Object source) {
		get().fireEventFromSource(event, source);
	}
	
	// hide constructor, use singleton pattern
	private MentorBus() {}
	
	private static EventBus instance = null;
	
}
