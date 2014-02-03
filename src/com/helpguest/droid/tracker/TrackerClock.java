/**
 * 
 */
package com.helpguest.droid.tracker;


/**
 * @author mabrams
 *
 */
public class TrackerClock implements ITrackerClock {

	private long startTime = 0;
	private long stopTime = 0;
	private boolean isStarted = false;
	private String note;
	
	/**
	 * Create a tracker clock from known times. It is not recommended to use this 
	 * ctor unless you are restoring a TrackerClock from persistence.
	 * @param startTime
	 * @param stopTime
	 */
	public TrackerClock(long startTime, long stopTime) {
		this.startTime = startTime;
		this.stopTime = stopTime;
		isStarted = startTime > 0 ? true : false;
	}
	
	/**
	 * Default CTor
	 */
	public TrackerClock() {}

	/* (non-Javadoc)
	 * @see com.helpguest.droid.tracker.ITrackerClock#start(com.helpguest.droid.tracker.ITracker)
	 */
	@Override
	public ITrackerClock start(ITracker tracker)
			throws UnsupportedOperationException {
		if (isStarted) {
			throw new UnsupportedOperationException("Already started at '" + startTime + "'");
		}
		startTime = System.currentTimeMillis();
		isStarted = true;
		return this;
	}

	@Override
	public ITrackerClock start(ITracker tracker, String note)
			throws UnsupportedOperationException {
		this.note = note;
		return start(tracker);
	}

	/* (non-Javadoc)
	 * @see com.helpguest.droid.tracker.ITrackerClock#stop(com.helpguest.droid.tracker.ITracker)
	 */
	@Override
	public long stop(ITracker tracker) {
		stopTime = System.currentTimeMillis();
		return startTime - stopTime;
	}

	/* (non-Javadoc)
	 * @see com.helpguest.droid.tracker.ITrackerClock#stop()
	 */
	@Override
	public long stop() {
		return stop(null);
	}

	/* (non-Javadoc)
	 * @see com.helpguest.droid.tracker.ITrackerClock#isStarted()
	 */
	@Override
	public boolean isStarted() {
		return isStarted;
	}

	/* (non-Javadoc)
	 * @see com.helpguest.droid.tracker.ITrackerClock#isExpired()
	 */
	@Override
	public boolean isExpired() {				
		return startTime > 0 && stopTime > 0;
	}

	/* (non-Javadoc)
	 * @see com.helpguest.droid.tracker.ITrackerClock#startTime()
	 */
	@Override
	public Long startTime() {
		if (startTime > 0) {
			return Long.valueOf(startTime);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.helpguest.droid.tracker.ITrackerClock#stopTime()
	 */
	@Override
	public Long stopTime() {
		if (stopTime > 0) {
			return Long.valueOf(stopTime);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.helpguest.droid.tracker.ITrackerClock#duration()
	 */
	@Override
	public long duration() {		
		return stopTime - startTime;
	}

	@Override
	public long currentRuntime() {
		if (isStarted && !isExpired()) {
			return System.currentTimeMillis() - startTime;
		}
		return 0;
	}

	@Override
	public String getNote() {
		return note;
	}

	@Override
	public void setNote(String note) {
		this.note = note;
	}	
	

}
