package hr.vsite.mentor;

import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;

/**
 * Daemon class, managed by procrun/jsvc.
 * See: http://commons.apache.org/proper/commons-daemon/
 */
public class MentorDaemon implements Daemon {

	@Override
	public void init(DaemonContext context) throws DaemonInitException, Exception {
		if (instance != null)
			throw new IllegalStateException("Mentor already initialized!");
		instance = Mentor.get();
		instance.init();
	}

	@Override
	public void start() throws Exception {
		if (instance == null)
			throw new IllegalStateException("Mentor not initialized!");
		instance.start();
	}

	@Override
	public void stop() throws Exception {
		if (instance == null)
			throw new IllegalStateException("Mentor not initialized!");
		instance.stop();
	}

	@Override
	public void destroy() {
		if (instance == null)
			throw new IllegalStateException("Mentor not initialized!");
		instance.destroy();
	}

	private static Mentor instance = null;
	
}
