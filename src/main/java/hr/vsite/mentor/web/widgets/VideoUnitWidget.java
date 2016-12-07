package hr.vsite.mentor.web.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.media.client.Video;
import com.google.gwt.resources.client.CssResource;

import hr.vsite.mentor.unit.VideoUnit;

import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialBadge;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialLabel;

public class VideoUnitWidget extends UnitWidget {

	public interface Resources extends UnitWidget.Resources {
		@Source({UnitWidget.Style.DefaultCss, "VideoUnitWidget.gss"})
		public Style style();
	}

	@CssResource.ImportedWithPrefix("VideoUnitWidget")
	public interface Style extends UnitWidget.Style {
		String video();
	}
	
	public VideoUnitWidget(VideoUnit unit) {

		super();
		
		timeBadge = new MaterialBadge();
		timeBadge.addStyleName(res.style().badge());
		getTitlePanel().insert(timeBadge, 0);

		MaterialColumn videoColumn = new MaterialColumn();
		videoColumn.setGrid("s12");
		videoColumn.setTextAlign(TextAlign.CENTER);
			video = Video.createIfSupported();
			if (video == null) {
				MaterialLabel message = new MaterialLabel("This browser does not support video playback.");
				message.addStyleName("flow-text");
				videoColumn.add(message);
			} else {
				video.addStyleName("responsive-video");
				video.setAutoplay(false);
				video.setControls(true);
				video.setLoop(false);
				video.setMuted(false);
				videoColumn.add(video);
			}
		getContentRow().add(videoColumn);
	
		if (unit != null)
			setUnit(unit);
		
	}

	public VideoUnit getUnit() { return (VideoUnit) super.getUnit(); }

	public void setUnit(VideoUnit unit) {

		super.setUnit(unit);
		
		String durationText = "? min";
		if (unit.getAttributes().getDuration() > 0)
			if (Math.round(unit.getAttributes().getDuration() / 60.0) >= 60)
				durationText = ">1 sat";
			else if (unit.getAttributes().getDuration() / 60 > 1)
				durationText = ((int) Math.round(unit.getAttributes().getDuration() / 60.0)) + " min";
			else
				durationText = unit.getAttributes().getDuration() + " min";
		timeBadge.setText(durationText);

		if (video != null) {
			video.setSrc(GWT.getHostPageBaseURL() + "api/units/" + unit.getId() + "/content");
			// set poster?
		}
		
	}

	private final MaterialBadge timeBadge;
	private final Video video;
	
	private static final Resources res = GWT.create(Resources.class);
	static {
		res.style().ensureInjected();
	}

}
