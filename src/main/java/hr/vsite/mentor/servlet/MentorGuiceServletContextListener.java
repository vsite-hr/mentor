package hr.vsite.mentor.servlet;

import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

import hr.vsite.mentor.MentorInjector;

/**
 * Creates Guice Injector that will manage all servlets and filters.
 */
public class MentorGuiceServletContextListener extends GuiceServletContextListener {

	@Override
	protected Injector getInjector() {
		return MentorInjector.get().createChildInjector(new MentorServletModule());
	}

}
