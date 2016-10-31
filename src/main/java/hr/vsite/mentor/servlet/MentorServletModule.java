package hr.vsite.mentor.servlet;

import javax.inject.Singleton;

import org.jboss.resteasy.plugins.server.servlet.HttpServlet30Dispatcher;

import com.google.inject.servlet.ServletModule;

import hr.vsite.mentor.servlet.rest.GuiceFilterDispatcher;

public class MentorServletModule extends ServletModule {

	@Override
	protected void configureServlets() {
		
		bind(HttpServlet30Dispatcher.class).in(Singleton.class);
		serve("/api/*").with(HttpServlet30Dispatcher.class);

		bind(GuiceFilterDispatcher.class).in(Singleton.class);
		filter("/api/*").through(GuiceFilterDispatcher.class);

	}

}
