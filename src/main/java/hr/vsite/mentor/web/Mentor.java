package hr.vsite.mentor.web;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;

import hr.vsite.mentor.web.activities.MentorActivityMapper;
import hr.vsite.mentor.web.theme.Theme;

import gwt.material.design.client.ui.MaterialToast;

public class Mentor implements EntryPoint {
	
	public static void setTitle(String placeSuffix) {
		Window.setTitle(Theme.messages().title(placeSuffix));
	}

	@Override
	public void onModuleLoad() {
	
		GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
			public void onUncaughtException(Throwable e) {
				Log.log(Level.SEVERE, e.getMessage(), e);
				MaterialToast.fireToast("Unknown error, please reload application");
			}
		});

		Theme.bundle();	// inject

    	workPanel = new SimplePanel();
    	workPanel.setSize("100%", "100%");

		ActivityMapper activityMapper = new MentorActivityMapper();
		ActivityManager activityManager = new ActivityManager(activityMapper, MentorBus.get());
		activityManager.setDisplay(workPanel);

		Element loader = Document.get().getElementById("loader");
		if (loader != null)
			loader.removeFromParent();
		
		RootPanel.get().add(workPanel, 0, 0);

        Places.handler().handleCurrentHistory();	// goes to the place represented on URL, else default place

	}
	
	private static final Logger Log = Logger.getLogger(Mentor.class.getName());
	
	private static SimplePanel workPanel;
	
}
