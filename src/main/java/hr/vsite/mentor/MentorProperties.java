package hr.vsite.mentor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Main Mentor properties.
 * Since it is used in configuring Guice DI, it can not be itself managed by Guice.
 */
public class MentorProperties extends Properties {

	public static MentorProperties get() {
		if (instance == null)
			instance = new MentorProperties();
		return instance;
	}
	
	private MentorProperties() {

		super();

    	try (InputStream propertiesStream = ClassLoader.getSystemResourceAsStream("mentor.properties")) {
			load(propertiesStream);
			version = getProperty("version");
		} catch (IOException e) {
			throw new RuntimeException("Could not load properties", e);
		}

	}

	public String getVersion() { return version; }

	private static final long serialVersionUID = 1L;
	private static MentorProperties instance = null;
	
	private final String version;

}
