package hr.vsite.mentor.web.theme;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.resources.client.GwtCreateResource;
import com.google.gwt.resources.client.GwtCreateResource.ClassType;
import com.google.gwt.resources.client.ImageResource;

public interface MentorBundle extends ClientBundle {
	
	@NotStrict
	@Source("gwt-style.gss")
	public CssResource gwtStyle();
		
	@Source("style.gss")
	MentorStyle style();

	@ClassType(MentorMessages.class)
	GwtCreateResource<MentorMessages> messagesFactory();
	
	@Source("profile-background.jpg")
	ImageResource profileBackgroundImage();

}