package hr.vsite.mentor.error;

public class JsonResponseError implements MentorError {

	public JsonResponseError() {}
	
	public String getUserMessage() { return userMessage; }
	public void setUserMessage(String userMessage) { this.userMessage = userMessage; }
	public String getInternalMessage() { return internalMessage; }
	public void setInternalMessage(String internalMessage) { this.internalMessage = internalMessage; }
	public int getCode() { return code; }
	public void setCode(int code) { this.code = code; }
	public String getInfo() { return info; }
	public void setInfo(String info) { this.info = info; }
	
	private String userMessage;
	private String internalMessage;
	private int code;
	private String info;
}
