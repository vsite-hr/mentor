package hr.vsite.mentor.web.activities;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

import hr.vsite.mentor.web.places.LecturersPlace;
import hr.vsite.mentor.web.views.LecturersView;

public class LecturersActivity extends AbstractActivity {

	public LecturersActivity(LecturersPlace place) {
		this.place = place;
	}
	
	@Override
	public void start(final AcceptsOneWidget containerWidget, EventBus eventBus) {

		LecturersView.get().init(place);
		LecturersView.get().show(containerWidget);
		
	}

	private final LecturersPlace place;
	
}
