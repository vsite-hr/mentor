package hr.vsite.mentor;

import com.google.inject.Guice;
import com.google.inject.Injector;

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
//			new DaoModule(),
			new MentorModule()
		);
	}

	public Injector getInjector() { return injector; }

	private static MentorInjector instance = null;
	private final Injector injector;

}
