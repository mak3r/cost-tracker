package com.helpguest.droid.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.helpguest.droid.R;
import com.helpguest.droid.tracker.ITracker;
import com.helpguest.droid.tracker.ITrackerClock;

import android.content.Context;
import android.graphics.Color;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ClockSummary extends ArrayAdapter<ITracker> {
	public Context mContext;
	private ArrayList<ITracker> mTrackers;
	private Date mDate;
	private LayoutInflater inflater;

	public ClockSummary(Context context, Collection<ITracker> trackers, Date date) {
		super(context, R.layout.row_t_clocks_summary);
		mContext = context;
		mDate = date;
		inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//Initialize the class variable and populate only with trackers that have clocks on date
		mTrackers = new ArrayList<ITracker>();
		for (ITracker t : trackers) {
			Collection<ITrackerClock> tc = t.getTrackerClocks(date);
			if (tc != null && tc.size() > 0) {
				mTrackers.add(t);
			}
		}		

	}
	
	@Override
	public int getCount() {
		return mTrackers.size();
	}

	@Override
	public ITracker getItem(int position) {
		return mTrackers.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return createRow(getItem(position), mDate, parent);
	}

	private LinearLayout createRow(ITracker tracker, Date date, ViewGroup viewGroup) {
		ClockSummaryHelper csHelper = new ClockSummaryHelper();
		csHelper.row = (LinearLayout) inflater.inflate(R.layout.row_t_clocks_summary, viewGroup, false);
				
		TextView trackerName = (TextView) csHelper.row.findViewById(R.id.namePart);
		trackerName.setText(tracker.getName());
		trackerName.setTextColor(Color.BLACK);

		TextView trackerDuration = (TextView) csHelper.row.findViewById(R.id.durationPart);
		trackerDuration.setTextColor(Color.BLACK);

		Collection<ITrackerClock> tClocks = tracker.getTrackerClocks(date);
		long duration = 0;
		for (ITrackerClock tc : tClocks) {
			if (tc.isExpired()) {
				duration += tc.duration();
			}
		}
		trackerDuration.setText(DateUtils.formatElapsedTime(duration/1000));
		//If millis matter, we could do something like this instead
		//SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault());
		//trackerDuration.setText(sdf.format(new Date(duration)));
		
		return csHelper.row;
	}
	
	class ClockSummaryHelper {
		LinearLayout row;
	}
}