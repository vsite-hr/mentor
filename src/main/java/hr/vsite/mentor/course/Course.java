package hr.vsite.mentor.course;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import hr.vsite.mentor.servlet.rest.providers.ObjectMapperProvider;
import hr.vsite.mentor.user.User;

public class Course {
	
	public UUID getId() { return id; }
	public void setId(UUID id) { this.id = id; }
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
	public User getAuthor() { return author; }
	public void setAuthor(User author) { this.author = author; }
	
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
		Course other = (Course) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		
		try {
			return mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}

	}
	
	private UUID id;
	private String title;
	private String description;
	private User author;
	
	@JsonIgnore
	private static final ObjectMapper mapper;

	static {
		ObjectMapperProvider mapperProvider = new ObjectMapperProvider();
		mapper = mapperProvider.getContext(Course.class);
		}
}
