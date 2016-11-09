package hr.vsite.mentor.lecture;

import hr.vsite.mentor.course.Course;
import hr.vsite.mentor.user.User;

public class LectureFilter {

	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
	public User getAuthor() { return author; }
	public void setAuthor(User author) { this.author = author; }
	public Course getCourse() { return course; }
	public void setCourse(Course course) { this.course = course; }

	private String title;
	private String description;
	private User author;
	private Course course;
	
}
