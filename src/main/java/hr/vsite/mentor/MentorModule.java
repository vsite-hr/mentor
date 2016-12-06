package hr.vsite.mentor;

import java.text.SimpleDateFormat;

import javax.inject.Singleton;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.spi.JobFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class MentorModule extends AbstractModule {

	@Override 
	protected void configure() {
		bind(MentorEventBus.class);
		bind(JobFactory.class).to(GuiceJobFactory.class);
		bind(Garbageman.class);
		bind(MentorStatus.class);
	}

	@Provides
	@Singleton
	Scheduler provideScheduler(JobFactory jobFactory) throws SchedulerException {
		Scheduler scheduler = new StdSchedulerFactory("quartz.properties").getScheduler();
    	scheduler.setJobFactory(jobFactory);
		return scheduler;
	}

	@Provides
	@Singleton
	ObjectMapper provideObjectMapper() {
		return new ObjectMapper()
			.configure(SerializationFeature.WRAP_ROOT_VALUE, false)
			.configure(SerializationFeature.INDENT_OUTPUT, false)
			.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
			.configure(MapperFeature.USE_WRAPPER_NAME_AS_PROPERTY_NAME, true)
			.disable(MapperFeature.AUTO_DETECT_CREATORS)
			.disable(MapperFeature.AUTO_DETECT_FIELDS)
			.disable(MapperFeature.AUTO_DETECT_GETTERS)
			.disable(MapperFeature.AUTO_DETECT_IS_GETTERS)
			.setSerializationInclusion(Include.NON_NULL)
			.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"))
			;
	}

}
