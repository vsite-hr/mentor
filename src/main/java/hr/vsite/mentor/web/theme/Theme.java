package hr.vsite.mentor.web.theme;

import com.google.gwt.core.client.GWT;

// main icon: https://www.iconfinder.com/icons/309036/education_student_study_icon
// good cartoon clipart: http://www.clipartof.com/portfolio/toonaday
//		http://www.clipartof.com/portfolio/toonaday/illustration/cartoon-black-and-white-outline-design-of-a-group-of-nerds-talking-438900.html
//		http://www.clipartof.com/portfolio/toonaday/illustration/cartoon-black-and-white-lineart-school-girl-pretending-to-be-a-teacher-standing-by-a-chalk-board-1430774.html
//		http://www.clipartof.com/portfolio/toonaday/illustration/cartoon-black-and-white-outline-design-of-a-desktop-computer-teaching-1045424.html
//		http://www.clipartof.com/portfolio/toonaday/illustration/cartoon-black-and-white-outline-design-of-a-nerdy-school-boy-holding-an-apple-440148.html

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
