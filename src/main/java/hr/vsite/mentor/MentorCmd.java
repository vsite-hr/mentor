package hr.vsite.mentor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MentorCmd {

	public static void main(String[] args) throws Exception {

		Mentor mentor = Mentor.get();

		mentor.init();
		mentor.start();

		try {
        	BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        	String line;
        	while (true) {
        		line = in.readLine();	// TODO should wait for Ctrl+C instead
        		if ("stop".equalsIgnoreCase(line))
        			mentor.stop();
        		else if ("start".equalsIgnoreCase(line))
        			mentor.start();
        		else if ("quit".equalsIgnoreCase(line) || "q".equalsIgnoreCase(line))
        			break;
        	}   
		} catch (IOException e) {
			Log.error("Exception in main event loop", e);
		}

		mentor.stop();
		mentor.destroy();

	}

	private static final Logger Log = LoggerFactory.getLogger(MentorCmd.class);

}
