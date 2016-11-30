package hr.vsite.mentor.web.activities;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

import hr.vsite.mentor.web.places.WelcomePlace;
import hr.vsite.mentor.web.views.WelcomeView;

public class WelcomeActivity extends AbstractActivity {

	public WelcomeActivity(WelcomePlace place) {
		this.place = place;
	}
	
	@Override
	public void start(final AcceptsOneWidget containerWidget, EventBus eventBus) {

		WelcomeView.get().init();
		WelcomeView.get().show(containerWidget);
		
	}

	@SuppressWarnings("unused")
	private final WelcomePlace place;
	
}
