package hr.vsite.mentor;

public class MentorStatus {

	MentorStatus() {
		this.version = MentorProperties.get().getVersion();
	}

	public String getVersion() { return version; }

	private final String version;
	
}
