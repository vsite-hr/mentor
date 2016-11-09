package hr.vsite.mentor.unit;

import java.util.UUID;

import hr.vsite.mentor.user.User;

public abstract class Unit {

	public UUID getId() { return id; }
	public void setId(UUID id) { this.id = id; }
	public UnitType getType() { return type; }
	public void setType(UnitType type) { this.type = type; }
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
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
		Unit other = (Unit) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	private UUID id;
	private UnitType type;
	private String title;
	private User author;

}
