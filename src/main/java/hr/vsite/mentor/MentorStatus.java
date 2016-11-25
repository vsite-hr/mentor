package hr.vsite.mentor;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MentorStatus {

	MentorStatus() {
		this.version = MentorProperties.get().getVersion();
	}

	@JsonProperty
	public String getVersion() { return version; }

	private final String version;
	
}
