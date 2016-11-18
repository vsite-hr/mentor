package hr.vsite.mentor.error;

public interface MentorError {

	public String getUserMessage();
	public void setUserMessage(String userMessage);
	public String getInternalMessage();
	public void setInternalMessage(String internalMessage);
	public int getCode();
	public void setCode(int code);
	public String getInfo();
	public void setInfo(String info);
}
