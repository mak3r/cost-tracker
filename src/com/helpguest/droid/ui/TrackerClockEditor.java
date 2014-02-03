package com.helpguest.droid.ui;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.helpguest.droid.R;
import com.helpguest.droid.tracker.ITrackerClock;

public final class TrackerClockEditor extends View {

	/**
	 * Since ITrackerClocks have both start and stop time,
	 * we need to know which Date to use on this view
	 *
	 */
	public enum Boundary {START, STOP}
	
	private LayoutInflater mInflater;
	private DatePicker mDatePicker;
	private TimePicker mTimePicker;
	private ITrackerClock mTClock;
	private Boundary mBoundary = Boundary.START;
	private LinearLayout mRootView;	
	
	public TrackerClockEditor(Context context) {
		this(context, null);
	}

	public TrackerClockEditor(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TrackerClockEditor(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TrackerClockEditor, 0, 0);

		try {
			mBoundary = (a.getInteger(R.styleable.TrackerClockEditor_Boundary, Boundary.START.ordinal()) == Boundary.START.ordinal() ? Boundary.START : Boundary.STOP);		       
		} finally {
			a.recycle();
		}		
		   
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mRootView = (LinearLayout) mInflater.inflate(R.layout.clock_time_and_date_editor, null);
		
		if (!isInEditMode()) {
			mDatePicker = (DatePicker) mRootView.findViewById(R.id.datePicker1);
			mTimePicker = (TimePicker) mRootView.findViewById(R.id.timePicker1);
		}
		//Add Button Click listener
		// Save back to the tracker clock when the button is clicked.
	}

	private void updatePickers() {
		Long dateTime = mBoundary == Boundary.START ? mTClock.startTime() : mTClock.stopTime();
		Date date = (dateTime == null ? new Date() : new Date(dateTime));
		
		Calendar cal = Calendar.getInstance();	
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int min = cal.get(Calendar.MINUTE);
		cal.setTime(date);

		mDatePicker.updateDate(year, month, day);
		mTimePicker.setCurrentHour(hour);
		mTimePicker.setCurrentMinute(min);
		
		invalidate();
		requestLayout();
	}
	
	/**
	 * Convenience method to set the clock and boundary simultaneously so that 
	 * the view gets invalidated only once instead of once for each parameter.
	 * This is more efficient than setting TrackerClock and Boundary individually
	 * @param tClock ITrackerClock which provides the time
	 * @param boundary of which time to use
	 */
	public void setClockForBoundary(ITrackerClock tClock, Boundary boundary) {
		mTClock = tClock;
		mBoundary = boundary;
		updatePickers();
	}
	
	public void setTrackerClock(ITrackerClock tClock) {
		mTClock = tClock;
		updatePickers();
	}

	public ITrackerClock getTrackerClock() {
		return mTClock;
	}

	public Boundary getType() {
		return mBoundary;
	}
	
	public void setType(Boundary type) {
		mBoundary = type;
		updatePickers();
	}

}
