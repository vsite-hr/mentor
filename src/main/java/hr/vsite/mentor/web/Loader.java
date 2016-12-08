package hr.vsite.mentor.web;

import java.util.Arrays;
import java.util.Collection;
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

	public static <E extends Enum<E>> Loader<E> start(Class<E> clazz) {
		return start(clazz.getEnumConstants());
	}

	@SafeVarargs
	public static <E extends Enum<E>> Loader<E> start(E... components) {
		return start(Arrays.asList(components));
	}

	public static <E extends Enum<E>> Loader<E> start(Collection<E> components) {
		return new Loader<E>(components);
	}

	private Loader(Collection<E> components) {
		requests = new HashMap<>();
		for (E component : components)
			requests.put(component, null);
		pending = new HashSet<>(components);
		successes = new HashSet<>();
		errors = new HashSet<>();
		finished = new HashSet<>();
		cancelled = new HashSet<>();
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
		if (!requests.containsKey(component))
			throw new IllegalArgumentException("Unknown loading component: " + component.toString());
		if (requests.get(component) != null)
			throw new IllegalStateException("Loading component " + component.toString() + " initialized more then once");
		requests.put(component, request);
		return this;
	}
	
	public void setLoadedHandler(LoadedHandler loadedHandler) {
		this.loadedHandler = loadedHandler;
	}
	
	public Loader<E> success(E component) {
		if (!requests.containsKey(component))
			throw new IllegalArgumentException("Unknown loading component: " + component.toString());
		if (requests.get(component) == null)
			throw new IllegalStateException("Loading component " + component.toString() + " not initialized");
		successes.add(component);
		finishAndCheckDone(component);
		return this;
	}

	public Loader<E> error(E component) {
		if (!requests.containsKey(component))
			throw new IllegalArgumentException("Unknown loading component: " + component.toString());
		if (requests.get(component) == null)
			throw new IllegalStateException("Loading component " + component.toString() + " not initialized");
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
	
	private final Map<E, Request> requests;
	private final Set<E> pending;
	private final Set<E> successes;
	private final Set<E> errors;
	private final Set<E> finished;
	private final Set<E> cancelled;
	private boolean cancelOnError = false;
	private HasProgress progress = null;
	private ProgressType progressType = null;
	private LoadedHandler loadedHandler = null;
	
}
