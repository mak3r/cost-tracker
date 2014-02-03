package com.helpguest.droid;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.helpguest.droid.tracker.ITracker;
import com.helpguest.droid.tracker.ITrackerClock;
import com.helpguest.droid.tracker.ITrackerManager;
import com.helpguest.droid.tracker.OnTrackerManagerChangeListener;
import com.helpguest.droid.tracker.TrackerClock;
import com.helpguest.droid.tracker.TrackerEvent;

/**
 * A dummy fragment representing a section of the app, but that simply
 * displays dummy text.
 */
public class TrackerCollectionFragment extends Fragment implements OnTrackerManagerChangeListener {

	/**
     * The fragment argument representing the section number for this
     * fragment.
     */
    public static final String ARG_SECTION_NUMBER = "section_number";
	public static final String ARG_TRACKER_NAMES = "tracker_names";
	
	private ViewGroup container;
	private View rootView;
	private ITrackerManager mTrackerManager;
	private LinearLayout linearLayout;
	private String currentTrackerName;
	private Map<String, ToggleButton> trackerButtonMap = new HashMap<String, ToggleButton>();

    public TrackerCollectionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	this.container = container;
        rootView = inflater.inflate(R.layout.fragment_tracker_collection, container, false);
		linearLayout = (LinearLayout) rootView.findViewById(R.id.linearLayoutTrackerCollection);

        return rootView;
    }

    @Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);		
		currentTrackerName = (String) ((ToggleButton)v).getTextOn();		
		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.menu_button_tracker_options, menu);		
	}

    @Override
	public boolean onContextItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
	    	case R.id.itemDelete:
	    	{
	    		//Log.d(TrackerCollectionFragment.class.toString(), "info.id: " + info.id);
	    		return mTrackerManager.remove(currentTrackerName);
	    	}
	    	case R.id.itemEdit:
	    	{
	    		((EditText) getActivity().findViewById(R.id.editTextTrackerName)).setText(currentTrackerName);
	    		break;
	    	}
	    	case R.id.itemHide:
	    	{
	    		ITracker tracker = mTrackerManager.getTracker(currentTrackerName);
	    		tracker.setVisible(false);
				mTrackerManager.update(tracker);
	    		break;
	    	}
	    	default:
	    		break;
    	}    
		return super.onContextItemSelected(item);
	}

	@Override
	public void onResume() {
		super.onResume();
        refreshLinearLayout();
	}
	
	private void refreshLinearLayout() {
		//TODO: can I remove this call to remove all views?
        linearLayout.removeAllViews();
        if (mTrackerManager != null) {
	        for (final String s : mTrackerManager.getTrackerNames()) {
				final ITracker tracker = mTrackerManager.getTracker(s);
				if (tracker.isVisible()) {
		        	ToggleButton tButton = new ToggleButton(container.getContext());
		        	tButton.setText(s + "(off)");
		        	tButton.setTextOn(s);
		        	tButton.setTextOff(s + "(off)");
					ITrackerClock tClock = tracker.getActiveTrackerClock();
					if (tClock != null && tClock.isStarted() && !tClock.isExpired()) {
						tButton.toggle();
					}
					if (tracker.isAutoStop()) {
						trackerButtonMap.put(s, tButton);
					}
		        	tButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							ITrackerClock tClock = tracker.getActiveTrackerClock();						
							if (isChecked) {
								//Stop any tracker clocks in the manual stop list.
								for (String s : trackerButtonMap.keySet()) {
									ITracker t = mTrackerManager.getTracker(s);
									ITrackerClock tc = t.getActiveTrackerClock();
									if (tc != null && tc.isStarted() && !tc.isExpired()) {
										tc.stop();
										trackerButtonMap.get(s).toggle();
									}
								}
								//Then start a clock for this tracker.
								if (tClock == null) {
									tClock = new TrackerClock();
									tracker.add(tClock);
								}
								tClock.start(null);
							} else  {
								if (tClock != null) {
									tClock.stop();
								}
							}
						}
		        		
		        	});
		        	linearLayout.addView(tButton);
			        registerForContextMenu(tButton);
		        }
	        }
        }
	}
	
	public void setTrackerManager(ITrackerManager trackerManager) {
		this.mTrackerManager = trackerManager;
	}

	@Override
	public void onTrackerChanged(TrackerEvent trackerEvent) {
		refreshLinearLayout();		
	}
    
    
}