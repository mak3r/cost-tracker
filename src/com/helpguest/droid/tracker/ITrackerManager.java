package com.helpguest.droid.tracker;

import java.util.Collection;
import java.util.Date;

public interface ITrackerManager extends IReadOnlyTrackerManager {

	public void add(Collection<ITracker> trackers);

	public ITracker getTracker(String name);
	
	public Collection<String> getTrackerNames();

	public Collection<ITracker> getTrackers();

	public Collection<ITracker> getTrackers(Date date);

	/**
	 * @param tracker to add to the collection in memory
	 */
	public void add(ITracker tracker);

	/**
	 * @param tracker to update
	 * @return the tracker that was replaced or null if the tracker passed in was null
	 */
	public ITracker update(ITracker tracker);
	
	/**
	 * 
	 * @param tracker to be removed
	 * @return true if an element was removed
	 */
	public boolean remove(ITracker tracker);

	/**
	 * 
	 * @param trackerName of Tracker to be removed
	 * @return true if this tracker is removed.
	 */
	public boolean remove(String trackerName);
	
	/**
	 * 
	 * @param listener to register for various CRUD events that happen in a TrackerManager
	 */
	public void setTrackerManagerChangeListener(OnTrackerManagerChangeListener listener);

}