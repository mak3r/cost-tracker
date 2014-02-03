package com.helpguest.droid.tracker.io;

import java.util.Collection;

import com.helpguest.droid.tracker.ITracker;

/**
 * Interface to writing trackers to persistent stores
 * @author da007ab
 *
 */
public interface ITrackerWriter {

	/**
	 * @param tracker to be written to persistence
	 */
	void write(ITracker tracker);

	/**
	 * 
	 * @param trackers Collection to write to the file
	 */
	void write(Collection<ITracker> trackers);
	
}
