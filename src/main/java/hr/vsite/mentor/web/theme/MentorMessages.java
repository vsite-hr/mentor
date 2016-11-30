package hr.vsite.mentor.web.theme;

import com.google.gwt.i18n.client.Messages;

/* Translations may be provided via properties files.
 * http://www.gwtproject.org/doc/latest/tutorial/i18n.html
 */
public interface MentorMessages extends Messages {

	@DefaultMessage("Mentor - {0}")
	String title(String place);

}
