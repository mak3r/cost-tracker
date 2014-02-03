package com.helpguest.droid.tracker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

public class SimpleTracker implements ITracker {

	private Collection<ITrackerClock> tClocks = new ArrayList<ITrackerClock>();
	private String name;
	private String description;
	private boolean mVisible = true;
	private boolean mAutoStop = true;
	
	/**
	 * 
	 * @param name of this SimpleTracker
	 */
	public SimpleTracker(String name) {
		this.name = name;
	}
	
	public SimpleTracker(String name, String description) {
		this(name);
		this.description = description;
	}
	
	/**
	 * Add a tracker clock to this SimpleTracker
	 * @param tClock to add
	 */
	@Override
	public void add(ITrackerClock tClock) {
		if (tClock != null) {
			tClocks.add(tClock);
		}
	}
	
	@Override
	public void add(Collection<ITrackerClock> trackerClocks) {
		tClocks.addAll(trackerClocks);
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public Collection<ITrackerClock> getAllTrackerClocks() {
		return tClocks;
	}

	@Override
	public Collection<ITrackerClock> getTrackerClocks(Date date) {
		Collection<ITrackerClock> datedClocks = new ArrayList<ITrackerClock>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
		String formattedDate = sdf.format(date);
		for (ITrackerClock tc : tClocks) {
			Date tcDate = new Date(tc.startTime().longValue());
			if (formattedDate != null &&  formattedDate.equals(sdf.format(tcDate))) {
				datedClocks.add(tc);
			}
		}
		return datedClocks;
	}

	@Override
	public ITrackerClock getActiveTrackerClock() {
		for (ITrackerClock tc : tClocks) {
			if ( !tc.isStarted() || (tc.isStarted() && !tc.isExpired()) ) {
				return tc;
			}
		}
		return null;
	}

	@Override
	public void setVisible(boolean visible) {
		mVisible = visible;
	}
	
	@Override
	public boolean isVisible() {
		return mVisible;
	}

	@Override
	public boolean isAutoStop() {
		return mAutoStop;
	}

	@Override
	public void setAutoStop(boolean autoStop) {
		mAutoStop = autoStop;
	}

}
