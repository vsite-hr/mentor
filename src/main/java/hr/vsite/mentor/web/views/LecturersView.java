package hr.vsite.mentor.web.views;

import java.util.List;
import java.util.logging.Logger;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.user.client.ui.AcceptsOneWidget;

import hr.vsite.mentor.user.User;
import hr.vsite.mentor.web.Loader;
import hr.vsite.mentor.web.places.LecturersPlace;
import hr.vsite.mentor.web.services.Api;
import hr.vsite.mentor.web.widgets.LecturerCard;
import hr.vsite.mentor.web.widgets.LecturersBanner;

import gwt.material.design.client.constants.ProgressType;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.client.ui.animate.MaterialAnimation;
import gwt.material.design.client.ui.animate.Transition;

public class LecturersView extends MaterialPanel {

	private static enum LoadComponent {
		Lecturers
	};

	public static LecturersView get() {
		if (instance == null)
			instance = new LecturersView();
		return instance;
	}
	
//	public interface Resources extends ApplicationShell.Resources {
//	    @Source(value = { ApplicationShell.Style.DefaultCss, "ClassroomView.gss" })
//	    LecturersView.Style style();
//	}
//
//	@CssResource.ImportedWithPrefix("ClassroomView")
//	public interface Style extends ApplicationShell.Style {
//		String filtersPanel();
//		String filterKey();
//		String filterValue();
//	}
	
	private LecturersView() {
		
		super();

		MaterialRow bannerRow = new MaterialRow();
			MaterialColumn bannerColumn = new MaterialColumn();
			bannerColumn.setGrid("s12 m12 l8");
			bannerColumn.setOffset("l2");
				LecturersBanner banner = new LecturersBanner();
			bannerColumn.add(banner);
		bannerRow.add(bannerColumn);
		add(bannerRow);
		
		MaterialRow lecturersRow = new MaterialRow();
			MaterialColumn lecturersColumn = new MaterialColumn();
			lecturersColumn.setGrid("s12 m10");
			lecturersColumn.setOffset("s0 m1");
				lecturersContainerRow = new MaterialRow();
			lecturersColumn.add(lecturersContainerRow);
		lecturersRow.add(lecturersColumn);
		add(lecturersRow);
		
	}
	
	public void show(AcceptsOneWidget containerWidget) {
		containerWidget.setWidget(ApplicationShell.get().wrap(this));
	}

	public void init(LecturersPlace place) {

		lecturersContainerRow.clear();

		lecturers = null;
		
		// TODO cancel loader if still running

		Loader<LoadComponent> loader = new Loader<LoadComponent>()
			.setCancelOnError(true)
			.setProgress(ApplicationShell.get(), ProgressType.INDETERMINATE);
		loader.add(LoadComponent.Lecturers, Api.get().user().list(null, null, 100, 0, new MethodCallback<List<User>>() {
			@Override
			public void onSuccess(Method method, List<User> lecturers) {
				LecturersView.this.lecturers = lecturers;
				if (lecturers == null) {
					loader.error(LoadComponent.Lecturers);
					MaterialToast.fireToast("Nije moguće učitati predavače!");
					return;
				}
				for (User lecturer : lecturers) {
					MaterialColumn column = new MaterialColumn();
					column.setGrid("s12 m6 l3");
						LecturerCard card = new LecturerCard(lecturer);
					column.add(card);
					lecturersContainerRow.add(column);
				}
				MaterialAnimation animation = new MaterialAnimation();
		        animation.setTransition(Transition.SHOW_GRID);
		        animation.animate(lecturersContainerRow);
				loader.success(LoadComponent.Lecturers);
			}
			@Override
			public void onFailure(Method method, Throwable exception) {
				loader.error(LoadComponent.Lecturers);
				MaterialToast.fireToast("Nije moguće učitati predavače! " + exception);
			}
		}));
		
	}

	private final MaterialRow lecturersContainerRow;

	@SuppressWarnings("unused")
	private List<User> lecturers = null;
	
	private static LecturersView instance = null;
	
	@SuppressWarnings("unused")
	private static final Logger Log = Logger.getLogger(LecturersView.class.getName());
//	private static final Resources res = GWT.create(Resources.class);
//	static {
//		res.style().ensureInjected();
//	}

}
