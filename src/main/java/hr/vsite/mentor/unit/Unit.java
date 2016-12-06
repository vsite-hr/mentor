package hr.vsite.mentor.unit;

import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import hr.vsite.mentor.MentorConfiguration;
import hr.vsite.mentor.user.User;

@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
public abstract class Unit {

	public static enum Type {
		Text,
		Video,
		Audio,
		Image,
		Quiz,
		Series,
		YouTube
	}
	
	public static Path getDataFolder() {
		return MentorConfiguration.get().getSubDataPath("unit");
	}

	@JsonProperty
	public UUID getId() { return id; }
	public void setId(UUID id) { this.id = id; }
	@JsonProperty
	public Type getType() { return type; }
	public void setType(Type type) { this.type = type; }
	@JsonProperty
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	@JsonProperty
	public User getAuthor() { return author; }
	public void setAuthor(User author) { this.author = author; }
	@JsonProperty
	public List<String> getKeywords() { return keywords; }
	public void setKeywords(List<String> keywords) { this.keywords = keywords; }
	@JsonProperty
	public Object getAttributes() { return attributes; }
	public void setAttributes(Object attributes) { this.attributes = attributes; }

	/** Override if derived unit can provide thumbnail */
	public Path getThumbnailPath() { return null; }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Unit other = (Unit) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	private UUID id;
	private Type type;
	private String title;
	private User author;
	private List<String> keywords;
	private Object attributes;
	
}
