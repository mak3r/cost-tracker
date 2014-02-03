package com.helpguest.droid.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.helpguest.droid.R;
import com.helpguest.droid.tracker.ITracker;
import com.helpguest.droid.tracker.ITrackerClock;

/**
 * 
 * @author mabrams
 *
 * ExpandableListAdapter that enables the individual tracker clocks
 * which are summarized to be shown and click-able for editing.
 * Editing of TrackerClocks is handled by a different layout
 */
public class ClockDetailByDate extends BaseExpandableListAdapter {

	private Context mContext;
	private Date mDate;
	private ArrayList<ITracker> mTrackers;
	private Map<ITracker, ArrayList<ITrackerClock>> parentChildrenMap = new HashMap<ITracker, ArrayList<ITrackerClock>>();
	private ClockSummary mClockSummary;
	private LayoutInflater inflater;

	public ClockDetailByDate(Context context, ArrayList<ITracker> trackers, Date date) {
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		mContext = context;
		mTrackers = trackers;
		mDate = date;
		mClockSummary = new ClockSummary(context, trackers, date);
		initializeParentChildRelationship();		
	}
	
	private void initializeParentChildRelationship() {
		for (ITracker t : mTrackers) {
			ArrayList<ITrackerClock> tClocks = new ArrayList<ITrackerClock>(t.getTrackerClocks(mDate));
			Collections.sort(tClocks, new Comparator<ITrackerClock>() {

				@Override
				public int compare(ITrackerClock lhs, ITrackerClock rhs) {
					Date lhsDate = new Date(lhs.startTime().longValue());
					Date rhsDate = new Date(rhs.startTime().longValue());
					return lhsDate.compareTo(rhsDate);
				}
				
			});
			parentChildrenMap.put(t, tClocks);
		}		
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return parentChildrenMap.get(mTrackers.get(groupPosition)).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {		 
		return createChild(parentChildrenMap.get(mTrackers.get(groupPosition)).get(childPosition), parent);
	}
	
	private LinearLayout createChild(ITrackerClock tClock, ViewGroup viewGroup) {
		Child child = new Child();
		child.detail = (LinearLayout) inflater.inflate(R.layout.row_t_clock_detail, viewGroup, false);
				
		DateFormat sdf = SimpleDateFormat.getTimeInstance();
		TextView startTime = (TextView) child.detail.findViewById(R.id.clockDetailStartTime);
		startTime.setText(sdf.format(tClock.startTime().longValue()));
		startTime.setTextColor(Color.BLUE);

		TextView endTime = (TextView) child.detail.findViewById(R.id.clockDetailEndTime);
		if (tClock.isExpired()) {
			endTime.setText(sdf.format(tClock.stopTime().longValue()));
			endTime.setTextColor(Color.BLUE);
		} else {
			endTime.setText(mContext.getResources().getString(R.string.clock_is_running_symbol));
			endTime.setTextColor(Color.RED);
		}
		return child.detail;
	}


	@Override
	public int getChildrenCount(int groupPosition) {
		return parentChildrenMap.get(mTrackers.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mTrackers.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return parentChildrenMap.keySet().size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Handle isExpanded
		return mClockSummary.getView(groupPosition, convertView, parent);		
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	class Child {
		LinearLayout detail;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		super.registerDataSetObserver(observer);
	}
}
