package hr.vsite.mentor.course;

import hr.vsite.mentor.user.User;

public class CourseFilter {

	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
	public User getAuthor() { return author; }
	public void setAuthor(User author) { this.author = author; }

	private String title;
	private String description;
	private User author;
	
}
