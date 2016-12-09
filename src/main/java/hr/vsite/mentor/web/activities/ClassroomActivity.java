package hr.vsite.mentor.web.activities;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

import hr.vsite.mentor.web.places.ClassroomPlace;
import hr.vsite.mentor.web.views.ClassroomView;

public class ClassroomActivity extends AbstractActivity {

	public ClassroomActivity(ClassroomPlace place) {
		this.place = place;
	}
	
	@Override
	public void start(final AcceptsOneWidget containerWidget, EventBus eventBus) {

		ClassroomView.get().init(place);
		ClassroomView.get().show(containerWidget);
		
	}

	private final ClassroomPlace place;
	
}
