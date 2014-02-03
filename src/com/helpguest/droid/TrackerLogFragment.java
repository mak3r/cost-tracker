package com.helpguest.droid;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.ViewSwitcher;

import com.helpguest.droid.tracker.IReadOnlyTrackerManager;
import com.helpguest.droid.tracker.ITracker;
import com.helpguest.droid.ui.ClockDetailByDate;

public class TrackerLogFragment extends Fragment implements OnItemClickListener, OnClickListener, OnLongClickListener, OnDateChangeListener{

	private ViewSwitcher viewSwitcher;
	private View calendarView = null;
	private ListView listView = null;
	private View mEditorView = null;
	private IReadOnlyTrackerManager mTrackerManager;
	private ExpandableListView dayView;
	private CalendarView trackerCalendarView;
	private ClockDetailByDate mClockDetail;
	private static final String logClass = TrackerLogFragment.class.getName();
	    	
	public TrackerLogFragment() {
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.layout_tracker_logs, container, false);
		rootView.setOnClickListener(this);		
		rootView.setOnLongClickListener(this);
		viewSwitcher = (ViewSwitcher) rootView.getRootView();

		calendarView = inflater.inflate(R.layout.tracker_logs_calendar_expandable, (ViewGroup)rootView, false);
		calendarView.setOnClickListener(this);
		trackerCalendarView = ((CalendarView)calendarView.findViewById(R.id.trackerCalendarView));
		trackerCalendarView.setOnDateChangeListener(this);
		trackerCalendarView.setOnLongClickListener(this);
		dayView = (ExpandableListView)calendarView.findViewById(R.id.tcSummaryExpandableListView);
		viewSwitcher.addView(calendarView);

		listView = (ListView) inflater.inflate(R.layout.list_view_summary_by_date, (ViewGroup)rootView, false);
		listView.setOnItemClickListener(this);
		viewSwitcher.addView(listView);
//		mEditorView = (View) inflater.inflate(R.layout.tracker_clock_date_editor, (ViewGroup)rootView, false);
//		TabHost tabHost = (TabHost) mEditorView.findViewById(android.R.id.tabhost);
//		tabHost.setCurrentTab(0);
//		viewSwitcher.addView(mEditorView);
		
//		mClockDateEditor = (ViewSwitcher) inflater.inflate(R.layout.tracker_clock_date_editor, (ViewGroup)rootView);

		Calendar cal = Calendar.getInstance(Locale.getDefault());
		cal.setTimeInMillis(trackerCalendarView.getDate());
		onSelectedDayChange(trackerCalendarView, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

        return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mTrackerManager == null) {
			return;
		}
		
		List<Date> dates = new ArrayList<Date>(mTrackerManager.getTrackerDates());
		Collections.sort(dates);
//		listView.setAdapter(new SummaryByDate(getActivity().getApplicationContext(), dates, mTrackerManager));
//		listView.setAdapter(new ClockSummary(getActivity().getApplicationContext(), mT, date)));
	}

	@Override
	public void onSelectedDayChange(CalendarView arg0, int year, int month, int day) {
		if (mTrackerManager == null) {
			return;
		}

		Log.d(logClass, arg0.toString() + "[" + year + "/" + month + "/" + day + "]");
		Calendar cal = Calendar.getInstance(Locale.getDefault());
		cal.set(year, month, day);
		Date date = cal.getTime();
		ArrayList<ITracker> trackers = new ArrayList<ITracker>();
		Collection<ITracker> tByDate = mTrackerManager.getTrackers(date);
		if (tByDate != null) {
			trackers.addAll(tByDate);
		}
		mClockDetail = new ClockDetailByDate(
				getActivity().getApplicationContext(), 
				new ArrayList<ITracker>(trackers), 
				date);
		dayView.setAdapter(mClockDetail);
		dayView.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Log.d(logClass, v.toString() + " clicked");
		if (v == listView) {
			//Switch to calendar view
			viewSwitcher.showNext();
		} else {
			//for now because calendar view isn't supporting long click
			onLongClick(v);
		}
	}

	@Override
	public boolean onLongClick(View v) {
		Log.d(logClass, v.toString() + " long clicked");
		if (v == trackerCalendarView){
			//Switch to log view
			viewSwitcher.showNext();
		}		
		return true;
	}
	
	public void setTrackerManager(IReadOnlyTrackerManager trackerManager) {
		this.mTrackerManager = trackerManager;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
		Log.d(logClass, "AdapterView: " + arg0 + "; View " + view + "; position " + position + "; id " + id + ";" );
//		ITracker tracker = mClockSummary.getItem(position);
//		
//		DatePicker startDatePicker = (DatePicker) mClockSummary.findViewById(R.id.clockStartDatePicker);
//		Calendar mCal=Calendar.getInstance(Locale.getDefault());
//		mCal.setTime()
//		int year=mCal.get(Calendar.YEAR);
//		int month=mCal.get(Calendar.MONTH);
//		int day=mCal.get(Calendar.DAY_OF_MONTH);
//		int hour=mCal.get(Calendar.HOUR_OF_DAY);
//		int min=mCal.get(Calendar.MINUTE);
//		
//		startDatePicker.updateDate(year, month, day);
//		onClick(arg1);
	}


}