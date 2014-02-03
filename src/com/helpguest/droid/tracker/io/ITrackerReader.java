package com.helpguest.droid.tracker.io;

import java.util.Collection;

import com.helpguest.droid.tracker.ITracker;

/**
 * An interface to reading ITrackers from persistent stores
 * @author da007ab
 *
 */
public interface ITrackerReader {

	/**
	 * @return all trackers from persistence 
	 */
	Collection<ITracker> getTrackers();

}
