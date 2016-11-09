package hr.vsite.mentor;

import com.google.inject.Guice;
import com.google.inject.Injector;

import hr.vsite.mentor.db.JdbcModule;
import hr.vsite.mentor.servlet.MentorServletModule;

/**
 * Top level Guice Injector.
 */
public class MentorInjector {

	public static Injector get() {
		if (instance == null)
			instance = new MentorInjector();
		return instance.getInjector();
	}
	
	private MentorInjector() {
		injector = Guice.createInjector(
			new JdbcModule(),
			new MentorModule(),
			new MentorServletModule()
		);
	}

	public Injector getInjector() { return injector; }

	private static MentorInjector instance = null;
	private final Injector injector;

}
