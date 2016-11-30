package hr.vsite.mentor.web.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;

import gwt.material.design.client.base.HasProgress;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.ProgressType;
import gwt.material.design.client.ui.MaterialContainer;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialNavBar;
import gwt.material.design.client.ui.MaterialNavBrand;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialSearch;
import gwt.material.design.client.ui.MaterialToast;

public class ApplicationShell extends Composite implements HasProgress {

	static ApplicationShell get() {
		if (instance == null)
			instance = new ApplicationShell();
		return instance;
	}
	
	public static Resources res() { return res; }

	public interface Resources extends ClientBundle {
		@Source(Style.DefaultCss)
		public Style style();
	}

	@CssResource.ImportedWithPrefix("View")
	public interface Style extends CssResource {
	    String DefaultCss = "hr/vsite/mentor/web/views/view.gss";
		String view();
	}

	private ApplicationShell() {

		MaterialPanel view = new MaterialPanel();
			mainNavBar = new MaterialNavBar();
//			mainNavBar.setActivates("sideBar");
				MaterialNavBrand navBrand = new MaterialNavBrand("Mentor");
				navBrand.setPaddingLeft(20.0);
				navBrand.setPaddingRight(20.0);
				navBrand.setHref(GWT.getHostPageBaseURL());
			mainNavBar.add(navBrand);
				MaterialLink searchLink = new MaterialLink(IconType.SEARCH);
				searchLink.setFloat(Float.RIGHT);
			mainNavBar.add(searchLink);
		view.add(mainNavBar);
			MaterialNavBar searchNavBar = new MaterialNavBar();
			searchNavBar.setVisible(false);
				MaterialSearch search = new MaterialSearch();
				search.setBackgroundColor(Color.WHITE);
				search.setPlaceholder("Znam što želim");
				search.setShadow(1);
			searchNavBar.add(search);
		view.add(searchNavBar);
			workPanel = new MaterialContainer();
		view.add(workPanel);
//			MaterialFooter footer = new MaterialFooter();
//				MaterialLink vsiteLink = new MaterialLink("VsiTe");
//				vsiteLink.setHref("https://www.vsite.hr/");
//			footer.add(vsiteLink);
//		view.add(footer);
		
		initWidget(view);
		
		searchLink.addClickHandler(e -> {
			search.open();
		});
		
		search.addOpenHandler(event -> {
			mainNavBar.setVisible(false);
			searchNavBar.setVisible(true);
		});
		
		search.addCloseHandler(event -> {
			mainNavBar.setVisible(true);
			searchNavBar.setVisible(false);
		});
		
		search.addValueChangeHandler(e -> {
			MaterialToast.fireToast("TODO: Pretraga pojma \"" + e.getValue() + "\"");
			mainNavBar.setVisible(true);
			searchNavBar.setVisible(false);
		});
		
	}

	public IsWidget wrap(IsWidget widget) {
		workPanel.clear();
		workPanel.add(widget);
		return this;
	}

	@Override
	public void showProgress(ProgressType type) {
		mainNavBar.showProgress(type);
	}

	@Override
	public void setPercent(double percent) {
		mainNavBar.setPercent(percent);
	}

	@Override
	public void hideProgress() {
		mainNavBar.hideProgress();
	}

	private final MaterialNavBar mainNavBar;
	private final MaterialContainer workPanel;

	private static ApplicationShell instance = null;
	private static final Resources res = GWT.create(Resources.class);
	static {
		res.style().ensureInjected();
	}

}

