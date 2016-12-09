package hr.vsite.mentor.web.activities;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

import hr.vsite.mentor.web.Places;
import hr.vsite.mentor.web.places.CoursePlace;
import hr.vsite.mentor.web.views.CourseView;

public class CourseActivity extends AbstractActivity {

	public CourseActivity(CoursePlace place) {
		this.place = place;
	}
	
	@Override
	public void start(final AcceptsOneWidget containerWidget, EventBus eventBus) {

		if (place.getCourseId() == null) {
			Places.goHome();
			return;
		}
		
		CourseView.get().init(place);
		CourseView.get().show(containerWidget);
		
	}

	private final CoursePlace place;
	
}
