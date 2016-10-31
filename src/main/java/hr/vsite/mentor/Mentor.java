package hr.vsite.mentor;

import java.io.InputStream;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.xml.XmlConfiguration;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hr.vsite.mentor.MentorInjector;

public class Mentor {

	public static Mentor get() {
		if (instance == null)
			instance = new Mentor();
		return instance;
	}

	private Mentor() {}

	public void	init() throws Exception {
        
		Log.info("**************************************************");
		Log.info("Welcome to Mentor {}...", MentorProperties.get().getVersion());
		Log.info("**************************************************");

		eventBus = MentorInjector.get().getInstance(MentorEventBus.class);
		scheduler = MentorInjector.get().getInstance(Scheduler.class);
		
		try (InputStream istream = ClassLoader.getSystemResourceAsStream("jetty.xml")) {
			XmlConfiguration jettyConf = new XmlConfiguration(istream);
			server = Server.class.cast(jettyConf.configure());
		}
		Log.info("Jetty initialized");

		eventBus.post(new MentorInitEvent(this));
		
	}
	
	public void start() throws Exception {
		
		Log.debug("Mentor {} starting...", MentorProperties.get().getVersion());
		
		server.start();
		Log.info("Jetty started");

		scheduler.start();
		Log.info("Scheduler started");
		
		eventBus.post(new MentorStartEvent(this));
		
		Log.info("Mentor is up and runnin'", MentorProperties.get().getVersion());

	}
	
	public void stop() {
		
		Log.debug("Mentor {} stopping...", MentorProperties.get().getVersion());

		try {
			scheduler.standby();
			Log.info("Scheduler stopped");
		} catch (SchedulerException e) {
			Log.error("Unable to stop scheduler", e);
		}

		try {
			server.stop();
			Log.info("Jetty stopped");
		} catch (Exception e) {
			Log.error("Error stopping jetty", e);
		}
		
		eventBus.post(new MentorStopEvent(this));
		
		Log.info("Mentor stopped", MentorProperties.get().getVersion());

	}
	
	public void destroy() {

		Log.debug("Mentor {} is initializing shutdown...", MentorProperties.get().getVersion());
		
		server.destroy();
		Log.info("Jetty shut down");
		
		try {
			scheduler.shutdown();
			Log.info("Scheduler shut down");
		} catch (SchedulerException e) {
			Log.error("Unable to shutdown scheduler", e);
		}
		
		eventBus.post(new MentorDestroyEvent(this));
		eventBus.shutdown();
		
		Log.info("Mentor {} successfully shut down - bye!", MentorProperties.get().getVersion());
		
	}

	private static final Logger Log = LoggerFactory.getLogger(Mentor.class);
	private static Mentor instance = null;

	private MentorEventBus eventBus = null;
	private Scheduler scheduler = null;
	private Server server = null;
	
}
