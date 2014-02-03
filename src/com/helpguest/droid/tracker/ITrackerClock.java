package com.helpguest.droid.tracker;

/**
 * An interface to starting and stopping the clock for tracking time.
 * Implementations are intended to be disposable and not restartable.
 * @author da007ab
 *
 */
public interface ITrackerClock {

	/**
	 * 
	 * Start the clock for this tracker. Implementations should take care to only allow
	 * this method to be called once.
	 * 
	 * @param tracker that started this clock
	 * @return a reference to the started clock
	 * @throws UnsupportedOperationException when this ITrackerClock has already been started.
	 */
	ITrackerClock start(ITracker tracker) throws UnsupportedOperationException;

	/**
	 * Start the clock for this tracker. Implementations should take care to only allow
	 * this method to be called once. This method allows a note to be added when the TrackerClock starts
	 *
	 * @param tracker that started this clock
	 * @param note to attach to this TrackerClock
	 * @return a reference to the started clock
	 * @throws UnsupportedOperationException when this ITrackerClock has already been started.
	 */
	ITrackerClock start(ITracker tracker, String note) throws UnsupportedOperationException;
	
	/**
	 * 
	 * @param tracker is the ITracker which called stop or null
	 * if the stop is requested by a non-ITracker
	 * @return the duration since start as a long
	 */
	long stop(ITracker tracker);

	/**
	 * Convenience method for {@link #stop(ITrackerClock)} with null argument
	 * @return the duration since start as a long
	 */
	long stop();
	
	/**
	 * 
	 * @return true if this tracker's clock has a meaningful startTime;
	 */
	boolean isStarted();
	
	/**
	 * It is possible for an ITrackerClock to be started but not expired.
	 * @return true if this ITrackerClock has both a start and end time.
	 */
	boolean isExpired();

	/**
	 * 
	 * @return the start time of this tracker or null if it is not started
	 */
	Long startTime();

	/**
	 * 
	 * @return the last stop time of this tracker or null if it is currently running
	 */
	Long stopTime();
	
	/**
	 * Note that a duration of 0 is not a good indicator of whether this ITrackerClock is in 
	 * fact expired. Theoretically it's possible to have the clock start and stop in an unmeasurable
	 * amount of time. Fast processors, what can I say.
	 * @return the duration of this ITrackerClock iif it has both a start and a stop time otherwise 0
	 * 
	 */
	long duration();
	
	/**
	 * If a TrackerClock has been started but not stopped, one can get the 
	 * current runtime in millis using this method. If the TrackerClock {@link #isExpired()}
	 * then this method always returns 0
	 * @return the time since this ITrackerClock {@link #isStarted()} or 0 if this TrackerClock 
	 * has not been started
	 * or {@link #isExpired()}
	 */
	long currentRuntime();
	
	/**
	 * @return The optional note written to this TrackerClock
	 */
	String getNote();
	
	/**
	 * @param note is just that, a note attached to this TrackerClock
	 */
	void setNote(String note);



}
