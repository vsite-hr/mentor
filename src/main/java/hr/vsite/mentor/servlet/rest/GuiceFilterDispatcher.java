package hr.vsite.mentor.servlet.rest;

import javax.inject.Inject;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import org.jboss.resteasy.plugins.guice.ModuleProcessor;
import org.jboss.resteasy.plugins.server.servlet.Filter30Dispatcher;
import org.jboss.resteasy.spi.Registry;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * <p>
 * Substitute for org.jboss.resteasy.plugins.guice.GuiceResteasyBootstrapServletContextListener,
 * because listeners can't get app-wide Guice Injector (at least not if instantiated from web.xml what is what we use)
 * </p>
 */
public class GuiceFilterDispatcher extends Filter30Dispatcher {

	@Inject
	GuiceFilterDispatcher(Injector parentInjector) {
		this.parentInjector = parentInjector;
	}
	
	protected Module getModule() {
		return new ResourceModule();
	}

	@Override
	public void init(FilterConfig config) throws ServletException {

		config.getServletContext().setInitParameter("resteasy.servlet.mapping.prefix", "/api");
		config.getServletContext().setInitParameter("javax.ws.rs.Application", RestApplication.class.getName());
		config.getServletContext().setInitParameter("resteasy.logger.type", "SLF4J");
		
		super.init(config);

		Registry registry = getDispatcher().getRegistry();
		ResteasyProviderFactory providerFactory = getDispatcher().getProviderFactory();

		ModuleProcessor processor = new ModuleProcessor(registry, providerFactory);

		Injector injector = parentInjector.createChildInjector(getModule());

		processor.processInjector(injector);

		while (injector.getParent() != null) {
			injector = injector.getParent();
			processor.processInjector(injector);
		}      

	}
	
	private final Injector parentInjector;
	
}
