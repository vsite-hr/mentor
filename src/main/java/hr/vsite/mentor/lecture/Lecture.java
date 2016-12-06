package hr.vsite.mentor.lecture;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gwt.dom.client.Style.Unit;

import hr.vsite.mentor.user.User;

public class Lecture {

	@JsonProperty
	public UUID getId() { return id; }
	public void setId(UUID id) { this.id = id; }
	@JsonProperty
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	@JsonProperty
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
	@JsonProperty
	public User getAuthor() { return author; }
	public void setAuthor(User author) { this.author = author; }
	@JsonProperty
	public List<String> getKeywords() { return keywords; }
	public void setKeywords(List<String> keywords) { this.keywords = keywords; }

	
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
		Lecture other = (Lecture) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public String toString(){
		return ("[id: " + (id != null ? id.toString() : "") + ", " +
				"title:" + (title != null ? title : "") + "]");
	}

	private UUID id;
	private String title;
	private String description;
	private User author;
	private List<String> keywords;
	// TODO Add lecture_head_unit_id (insert(), update())
	// TODO thumbnail?

}
