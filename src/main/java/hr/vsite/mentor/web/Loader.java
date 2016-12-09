package hr.vsite.mentor.web;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.gwt.http.client.Request;

import gwt.material.design.client.base.HasProgress;
import gwt.material.design.client.constants.ProgressType;

public class Loader<E extends Enum<E>> {

	@FunctionalInterface
	public static interface LoadedHandler {
		void onLoaded();
	}

	public Set<E> getPending() { return pending; }
	public Set<E> getSuccesses() { return successes; }
	public Set<E> getErrors() { return errors; }
	public Set<E> getFinished() { return finished; }
	public Set<E> getCancelled() { return cancelled; }

	public boolean getCancelOnError() { return cancelOnError; }
	public Loader<E> setCancelOnError(boolean cancelOnError) {
		this.cancelOnError = cancelOnError;
		return this;
	}

	public HasProgress getProgress() { return progress; }
	public Loader<E> setProgress(HasProgress progress, ProgressType type) {
		this.progress = progress;
		this.progressType = type;
		progress.showProgress(type);
		return this;
	}

	public Loader<E> add(E component, Request request) {
		if (requests.containsKey(component))
			throw new IllegalStateException("Loading component " + component.toString() + " initialized more then once");
		requests.put(component, request);
		pending.add(component);
		return this;
	}
	
	public void setLoadedHandler(LoadedHandler loadedHandler) {
		this.loadedHandler = loadedHandler;
	}
	
	public Loader<E> success(E component) {
		if (!requests.containsKey(component))
			throw new IllegalArgumentException("Unknown loading component: " + component.toString());
		successes.add(component);
		finishAndCheckDone(component);
		return this;
	}

	public Loader<E> error(E component) {
		if (!requests.containsKey(component))
			throw new IllegalArgumentException("Unknown loading component: " + component.toString());
		errors.add(component);
		if (cancelOnError && pending.size() > 1)
			for (E pendingComponent : pending)
				if (pendingComponent != component) {
					requests.get(pendingComponent).cancel();
					cancelled.add(pendingComponent);
				}
		finishAndCheckDone(component);
		return this;
	}

	private boolean finishAndCheckDone(E component) {
		pending.remove(component);
		finished.add(component);
		if (progress != null && progressType == ProgressType.DETERMINATE)
			progress.setPercent(100.0 * finished.size() / requests.size());
		if (!pending.isEmpty())
			return false;
		if (progress != null)
			progress.hideProgress();
		if (loadedHandler != null && finished.size() == successes.size())
			loadedHandler.onLoaded();
		return true;
	}
	
	private final Map<E, Request> requests = new HashMap<>();
	private final Set<E> pending = new HashSet<>();
	private final Set<E> successes = new HashSet<>();
	private final Set<E> errors = new HashSet<>();
	private final Set<E> finished = new HashSet<>();
	private final Set<E> cancelled = new HashSet<>();
	private boolean cancelOnError = false;
	private HasProgress progress = null;
	private ProgressType progressType = null;
	private LoadedHandler loadedHandler = null;
	
}
