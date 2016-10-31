package hr.vsite.mentor;

import java.nio.file.FileSystems;

import java.nio.file.Path;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main Mentor configuration.
 * Since it is used in configuring Guice DI, it can not be itself managed by Guice.
 */
public class MentorConfiguration extends XMLConfiguration {

	public static MentorConfiguration get() {
		if (instance == null)
			instance = new MentorConfiguration();
		return instance;
	}
	
	private MentorConfiguration() {
    	
		super();
        
		try {
			setThrowExceptionOnMissing(true);
			setDelimiterParsingDisabled(true);
			load("mentor.xml");
            Log.info("Configuration loaded from {}", getFile().getAbsolutePath());
        } catch (ConfigurationException e) {
            throw new RuntimeException("Could not load configuration", e);
        }
		
		dataPath = FileSystems.getDefault().getPath(getString("mentor.DataPath"));
		Log.info("Data path is {}", dataPath);

	}

	public Path getDataPath() { return dataPath; }
	public Path getSubDataPath(String subfolder) { return dataPath.resolve(subfolder); }
	
    private static final Logger Log = LoggerFactory.getLogger(MentorConfiguration.class);
	private static final long serialVersionUID = 1L;
	private static MentorConfiguration instance = null;
	
    private final Path dataPath;

}
