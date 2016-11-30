package hr.vsite.mentor.web.theme;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.resources.client.GwtCreateResource;
import com.google.gwt.resources.client.GwtCreateResource.ClassType;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;

public interface MentorBundle extends ClientBundle {
	
	@NotStrict
	@Source("gwt-style.gss")
	public CssResource gwtStyle();
		
	@Source("style.gss")
	MentorStyle style();

	@ClassType(MentorMessages.class)
	GwtCreateResource<MentorMessages> messagesFactory();
	
//	@Source("noise.png")
//	@ImageOptions(repeatStyle=RepeatStyle.Both)
//	ImageResource backgroundImage();
//
//	@Source("logo.png")
//	@ImageOptions(repeatStyle=RepeatStyle.None)
//	ImageResource logoImage();
//
//	@Source("delete-16.png")
//	ImageResource delete16Image();

}