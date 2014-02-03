package com.helpguest.droid.ui;

import java.util.Collection;
import java.util.Date;

import android.content.Context;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.helpguest.droid.R;
import com.helpguest.droid.tracker.IReadOnlyTrackerManager;
import com.helpguest.droid.tracker.ITracker;

public class SummaryByDate extends ArrayAdapter<Date> {

	private java.text.DateFormat DF;
	private IReadOnlyTrackerManager mTrackerManager;
	private Context mContext;
	private Date[] mDates;
		
	/**
	 * 
	 * @param context the application context
	 * @param the dates to use to select which trackers and dates to display
	 * @param trackerManager which interfaces with the data store
	 */
	public SummaryByDate(Context context, Collection<Date> dates, IReadOnlyTrackerManager trackerManager) {
		super(context, R.layout.list_view_summary_by_date, dates.toArray(new Date[0]));
		mTrackerManager = trackerManager;
		mContext = context;
		mDates = dates.toArray(new Date[0]);

		DF = DateFormat.getDateFormat(mContext);		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {	
		LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		TClockSummary tcSummary = new TClockSummary();
		tcSummary.header = (LinearLayout) inflater.inflate(R.layout.layout_tracker_summary_by_date, parent, false);
		
		Collection<ITracker> trackers = mTrackerManager.getTrackers(getItem(position));
		if (trackers == null) {
			//Early return, no content
			return new LinearLayout(mContext);
		}

		TextView textViewDate = (TextView) tcSummary.header.findViewById(R.id.SBDdate);
		textViewDate.setText(DF.format(getItem(position)));
		textViewDate.setTextColor(Color.BLACK);

		tcSummary.rows = (ListView) tcSummary.header.findViewById(R.id.listViewSBDTrackers);
		tcSummary.rows.setAdapter(new ClockSummary(mContext, trackers, mDates[position]));
		tcSummary.header.removeAllViews();
		tcSummary.header.addView(tcSummary.rows);

//		for (ITracker t : trackers) {
//			tcSummary.header.addView(createRow(tcSummary.header, t, getItem(position)));
//		}

		return tcSummary.header;
	}

	
		
	class TClockSummary {
		LinearLayout header;
		ListView rows;
	}	
				
}
