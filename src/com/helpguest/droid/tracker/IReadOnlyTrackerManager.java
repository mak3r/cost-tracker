package com.helpguest.droid.tracker;

import java.util.Collection;
import java.util.Date;

public interface IReadOnlyTrackerManager {

	/* (non-Javadoc)
	 * @see com.helpguest.droid.tracker.ITrackerManager#getTracker(java.lang.String)
	 */
	public abstract ITracker getTracker(String name);

	/* (non-Javadoc)
	 * @see com.helpguest.droid.tracker.ITrackerManager#getTrackerNames()
	 */
	public abstract Collection<String> getTrackerNames();

	/* (non-Javadoc)
	 * @see com.helpguest.droid.tracker.ITrackerManager#getTrackers()
	 */
	public abstract Collection<ITracker> getTrackers();

	/* (non-Javadoc)
	 * @see com.helpguest.droid.tracker.ITrackerManager#getTrackers(java.util.Date)
	 */
	public abstract Collection<ITracker> getTrackers(Date date);
	
	/**
	 * Get the distinct collection of dates known to this ITrackerManager
	 * @return this Collection of dates
	 */
	public abstract Collection<Date> getTrackerDates();

}