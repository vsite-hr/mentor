package hr.vsite.mentor.web.theme;

import com.google.gwt.core.client.GWT;

// main icon: https://www.iconfinder.com/icons/309036/education_student_study_icon

public class Theme {

	public static MentorBundle bundle() { return bundle; }
	public static MentorMessages messages() { return messages; }

	private Theme() {}

	private static final MentorBundle bundle;
	private static final MentorMessages messages;

	static {

		bundle = GWT.create(MentorBundle.class);
		bundle.gwtStyle().ensureInjected();
		bundle.style().ensureInjected();

		messages = GWT.create(MentorMessages.class);
		
	}
	
}
