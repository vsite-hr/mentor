package hr.vsite.mentor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;

@Singleton
public class MentorEventBus extends AsyncEventBus {

	MentorEventBus() {
		this(Executors.newWorkStealingPool(), new SubscriberExceptionHandler() {
			@Override
			public void handleException(Throwable exception, SubscriberExceptionContext context) {
				Log.error("Error in event handler ({})", context, exception);
			}
		});
	}

	MentorEventBus(ExecutorService executor, SubscriberExceptionHandler handler) {
		super(executor, handler);
		this.executor = executor;
	}

	public void shutdown() {
		executor.shutdown();
		try {
			executor.awaitTermination(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			Log.error("Can't terminate event bus", e);
		}
	}
	
	private final ExecutorService executor;

	private static final Logger Log = LoggerFactory.getLogger(MentorEventBus.class);

}
