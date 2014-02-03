package com.helpguest.droid.tracker;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;

import android.content.Context;
import android.text.format.DateFormat;

/**
 * Utility methods to manage the Trackers. This utility will
 * provide an interface to the user to manage how, when and where
 * trackers are persisted so that clients don't have to deal with
 * those issues.
 * 
 * @author mabrams
 *
 */
public class InMemoryTrackerManager implements ITrackerManager {

	private static java.text.DateFormat SDF;
	private Collection<OnTrackerManagerChangeListener> listeners = new ArrayList<OnTrackerManagerChangeListener>();
	private Map<String, Collection<ITracker>> dateTrackerMap = new HashMap<String, Collection<ITracker>>();
	private Map<String, ITracker> nameTrackerMap = new HashMap<String, ITracker>();
	private Collection<Date> mDates = new HashSet<Date>();
	
	public InMemoryTrackerManager(Context appContext) {
		SDF = DateFormat.getDateFormat(appContext);
	}
	
	
	/* (non-Javadoc)
	 * @see com.helpguest.droid.tracker.ITrackerManager#add(java.util.Collection)
	 */
	@Override
	public void add(Collection<ITracker> trackers) {
		for (ITracker t : trackers) {
			add(t);		
		}
		if (trackers != null && trackers.size() > 0) {
			TrackerEvent trackerEvent = new TrackerEvent("Tracker Collection Added", TrackerEvent.Type.CREATED);
			notifyTrackerManagerChangeListeners(trackerEvent);
		}
	}


	private void loadDateMap(ITracker t) {
		for (ITrackerClock tc : t.getAllTrackerClocks()) {
			Long startTime = tc.startTime();
			if (startTime != null) {
				Date date = new Date(startTime);
				String dateString = SDF.format(date);
				Collection<ITracker> trackers = dateTrackerMap.remove(dateString);
				if (trackers == null) {
					trackers = new ArrayList<ITracker>();
				}
				if (!trackers.contains(t)) {
					trackers.add(t);
				} 
				dateTrackerMap.put(dateString, trackers);
				
				Calendar cal = Calendar.getInstance(Locale.getDefault());
				cal.setTime(date);
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				mDates.add(cal.getTime());
			}
		}
		
	}

	/* (non-Javadoc)
	 * @see com.helpguest.droid.tracker.ITrackerManager#getTracker(java.lang.String)
	 */
	@Override
	public ITracker getTracker(String name) {
		return nameTrackerMap.get(name);
	}

	/* (non-Javadoc)
	 * @see com.helpguest.droid.tracker.ITrackerManager#getTrackerNames()
	 */
	@Override
	public Collection<String> getTrackerNames() {
		return nameTrackerMap.keySet();
	}

	/* (non-Javadoc)
	 * @see com.helpguest.droid.tracker.ITrackerManager#getTrackers()
	 */
	@Override
	public Collection<ITracker> getTrackers() {
		return nameTrackerMap.values();
	}

	/* (non-Javadoc)
	 * @see com.helpguest.droid.tracker.ITrackerManager#getTrackers(java.util.Date)
	 */
	@Override
	public Collection<ITracker> getTrackers(Date date) {
		if (date == null) {
			return null;
		}
		String dateString = SDF.format(date);
		return dateTrackerMap.get(dateString);
	}
	
	/* (non-Javadoc)
	 * @see com.helpguest.droid.tracker.ITrackerManager#add(com.helpguest.droid.tracker.ITracker)
	 */
	@Override
	public void add(ITracker tracker) {
		if (tracker != null) {
			ITracker inMap = nameTrackerMap.get(tracker.getName());
			if (inMap != null) {
				//It's already in the map
				update(tracker);
			} else {
				nameTrackerMap.put(tracker.getName(), tracker);
				TrackerEvent trackerEvent = new TrackerEvent("Tracker Added", TrackerEvent.Type.CREATED);
				notifyTrackerManagerChangeListeners(trackerEvent);
			}
			loadDateMap(tracker);
		}

	}

	/* (non-Javadoc)
	 * @see com.helpguest.droid.tracker.ITrackerManager#update(com.helpguest.droid.tracker.ITracker)
	 */
	@Override
	public ITracker update(ITracker tracker) {
		if (tracker == null) {
			return null;
		}
		ITracker updatedTracker = nameTrackerMap.put(tracker.getName(), tracker);
		TrackerEvent trackerEvent = new TrackerEvent("Tracker Collection Updated", TrackerEvent.Type.UPDATED);
		notifyTrackerManagerChangeListeners(trackerEvent);
		return updatedTracker;
	}
	
	@Override
	public Collection<Date> getTrackerDates() {
		return mDates ;
	}
	
	@Override
	public void setTrackerManagerChangeListener(
			OnTrackerManagerChangeListener listener) {
		listeners.add(listener);		
	}

	@Override
	public boolean remove(String trackerName) {
		if (trackerName == null) {
			return false;
		}
		ITracker tracker = nameTrackerMap.get(trackerName);
		return remove(tracker);		
	}
	
	@Override
	public boolean remove(ITracker tracker) {
		boolean removed = false;
		if (tracker == null) {
			return false;
		}		
		if (nameTrackerMap.remove(tracker.getName()) != null) {
			removed = true;
		}
		if (removed) {
			for (ITrackerClock tc : tracker.getAllTrackerClocks()) {
				long startTime = tc.startTime().longValue();
				String date = SDF.format(new Date(startTime));
				Collection<ITracker> trackers = dateTrackerMap.remove(date);
				if (trackers != null) {
					trackers.remove(tracker);
					dateTrackerMap.put(date, trackers);
				} 		
			}	
			
			TrackerEvent trackerEvent = new TrackerEvent("Tracker Collection Removed", TrackerEvent.Type.DELETED);
			notifyTrackerManagerChangeListeners(trackerEvent);

		}
		return removed;
	}

	private void notifyTrackerManagerChangeListeners(TrackerEvent trackerEvent) {
		for (OnTrackerManagerChangeListener l : listeners) {
			l.onTrackerChanged(trackerEvent);
		}
	}


}
