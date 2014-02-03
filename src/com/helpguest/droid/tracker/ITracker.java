package com.helpguest.droid.tracker;

import java.util.Collection;
import java.util.Date;

/**
 * Interface to ITracker object which associates a name to a 
 * collection of ITrackerClock objects
 * @author da007ab
 *
 */
public interface ITracker {

	/**
	 * @return The name of this ITracker
	 */
	public String getName();
	
	/**
	 * @param name to set for this ITracker
	 */
	public void setName(String name);
	
	/**
	 * @return the short description of this Tracker
	 */
	public String getDescription();
	
	/**
	 * @param description to add to this Tracker
	 */
	public void setDescription(String description);
	
	/**
	 * @return all ITrackerClocks associated with this ITracker
	 */
	public Collection<ITrackerClock> getAllTrackerClocks();
	
	/**
	 * @param date to limit the collection by
	 * @return all {@link #ITrackerClock ITrackerClocks} for the given date
	 */
	public Collection<ITrackerClock> getTrackerClocks(Date date);

	/**
	 * 
	 * @param tClock to add to this ITracker
	 */
	public void add(ITrackerClock tClock);

	/**
	 * 
	 * @param trackerClocks Collection to add to this ITracker
	 */
	public void add(Collection<ITrackerClock> trackerClocks);
	
	/**
	 * An active tracker clock is one which has either never been started or
	 * has been started but not stopped.
	 * There should be 0 or 1 active tracker clocks. Multiple tracker clocks
	 * should not be possible.
	 * @return the active tracker clock or null if no tracker clock in this ITracker
	 * has been started but not stopped.
	 */
	public ITrackerClock getActiveTrackerClock();
	
	/**
	 * 
	 * @param visible set to true if this Tracker should be displayed in UI pages, false 
	 * to hide this Tracker in UI views
	 */
	public void setVisible(boolean visible);
	
	/**
	 * 
	 * @return true if this tracker should be visible in UI views otherwise false.
	 */
	public boolean isVisible();

	/**
	 * 
	 * @return true if this ITracker has clocks which will automatically be stopped when 
	 * a different ITracker's ITrackerClock in the same ITrackerManager is started
	 */
	public boolean isAutoStop();
	
	/**
	 * 
	 * @param autoStop flag is set to true to give an indication to the ITrackerManager to stop
	 * tracker clocks on this tracker when a different ITracker has it's clock started.
	 */
	public void setAutoStop(boolean autoStop);
}
