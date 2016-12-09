package hr.vsite.mentor.web.activities;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

import hr.vsite.mentor.web.Places;
import hr.vsite.mentor.web.places.LecturePlace;
import hr.vsite.mentor.web.views.LectureView;

public class LectureActivity extends AbstractActivity {

	public LectureActivity(LecturePlace place) {
		this.place = place;
	}
	
	@Override
	public void start(final AcceptsOneWidget containerWidget, EventBus eventBus) {

		if (place.getLectureId() == null || place.getCourseId() == null) {
			Places.goHome();
			return;
		}
		
		LectureView.get().init(place);
		LectureView.get().show(containerWidget);
		
	}

	private final LecturePlace place;
	
}
