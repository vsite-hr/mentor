package hr.vsite.mentor.unit;

import hr.vsite.mentor.lecture.Lecture;
import hr.vsite.mentor.user.User;

public class UnitFilter {

	public Unit.Type getType() { return type; }
	public void setType(Unit.Type type) { this.type = type; }
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	public User getAuthor() { return author; }
	public void setAuthor(User author) { this.author = author; }
	public Lecture getLecture() { return lecture; }
	public void setLecture(Lecture lecture) { this.lecture = lecture; }
	
	private Unit.Type type;
	private String title;
	private User author;
	private Lecture lecture;
	
}
