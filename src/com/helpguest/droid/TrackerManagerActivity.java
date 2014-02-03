package com.helpguest.droid;

import java.util.ArrayList;
import java.util.Locale;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import com.helpguest.droid.tracker.ITracker;
import com.helpguest.droid.tracker.ITrackerManager;
import com.helpguest.droid.tracker.OnTrackerManagerChangeListener;
import com.helpguest.droid.tracker.PersistentTrackerManager;
import com.helpguest.droid.tracker.SimpleTracker;
import com.helpguest.droid.tracker.TrackerEvent;

public class TrackerManagerActivity extends FragmentActivity implements OnTrackerManagerChangeListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

	private TrackerEditorFragment trackerEditorFragment;
	private TrackerCollectionFragment trackerCollectionFragment;
	private EditText mTrackerName;

	private ITrackerManager trackerManager;
	
    static enum Pages {TRACKER_LOG, TRACKERS, TRACKER_EDITOR};
    final static Pages[] PAGES = Pages.values();
    
    
	/**
	 * Name of the common preferences file
	 */
	public static final String COST_TRACKER_PREFS_FILE = "costTracker.prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tracker_manager);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        
        // Initialize the PersistentTrackerManager
        trackerManager = new PersistentTrackerManager(getApplicationContext(), getSharedPreferences(COST_TRACKER_PREFS_FILE, MODE_PRIVATE));
        trackerManager.setTrackerManagerChangeListener(this);
        
    }
    
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tracker_manager, menu);
        return true;
    }
    
    

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private TrackerLogFragment mTrackerLogFragment;

		public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a TrackerCollectionFragment (defined as a static inner class
            // below) with the page number as its lone argument.
        	Fragment fragment = null;
        	switch(PAGES[position])
        	{
        	case TRACKER_LOG:
        	{
        		mTrackerLogFragment = new TrackerLogFragment();
        		//TODO: use Bundle to pass args instead of specialized set method
        		mTrackerLogFragment.setTrackerManager(trackerManager);
        		fragment = mTrackerLogFragment;
        		break;
        	}
        	case TRACKERS:
        	{
        		trackerCollectionFragment = new TrackerCollectionFragment();
        		trackerCollectionFragment.setTrackerManager(trackerManager);
        		trackerManager.setTrackerManagerChangeListener(trackerCollectionFragment);
        		fragment = trackerCollectionFragment;
	            Bundle args = new Bundle();
	            args.putInt(TrackerCollectionFragment.ARG_SECTION_NUMBER, position + 1);
	            args.putStringArrayList(TrackerCollectionFragment.ARG_TRACKER_NAMES, new ArrayList<String>(trackerManager.getTrackerNames()));
	            fragment.setArguments(args);
	            break;        		
        	}
        	case TRACKER_EDITOR:
        	{
        		trackerEditorFragment = new TrackerEditorFragment();
        		fragment = trackerEditorFragment;
        		mViewPager.setOnPageChangeListener((TrackerEditorFragment) fragment);
        		break;
        	}        	
        	default:
        		//set the fragment to an ErrorFragment
        	};
        	
        	return fragment;
        }

        @Override
        public int getCount() {
            return PAGES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (PAGES[position]) {
                case TRACKER_LOG:
                    return getString(R.string.title_section1).toUpperCase(l);
                case TRACKERS:
                    return getString(R.string.title_section2).toUpperCase(l);
                case TRACKER_EDITOR:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    public void addTrackerClicked(View view) {
		Log.d(TrackerLogFragment.class.getName(), "Adding new tracker");		
	}
	
	public void updateTracker(View view) {
		Log.d(TrackerLogFragment.class.getName(), "Clicked OK on edit tracker page");
		mTrackerName = (EditText) findViewById(R.id.editTextTrackerName);
		ITracker tracker = new SimpleTracker(mTrackerName.getText().toString(), mTrackerName.getText().toString());
		trackerManager.add(tracker);	
		mViewPager.setCurrentItem(Pages.TRACKERS.ordinal(), true);
		
		//Clear the form
		((EditText) findViewById(R.id.editTextTrackerName)).setText("");
	}
	
	public void deleteTracker(View view) {
		Log.d(TrackerLogFragment.class.getName(), "Clicked OK on delete tracker menu item + view: " + view);
		
	}

    @Override
	public boolean onContextItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
	    	case R.id.itemEdit:
	    	{
	    		mViewPager.setCurrentItem(Pages.TRACKER_EDITOR.ordinal(), true);
	    		//return false so that the Fragment has a chance to handle this case also.
	    		return false;
	    	}
	    	case R.id.itemView:
	    	{
	    		mViewPager.setCurrentItem(Pages.TRACKER_LOG.ordinal(), true);
	    		return true;
	    	}
	    	default:
	    	{
	        	return super.onContextItemSelected(item);
	    	}
    	}
    
	}

	@Override
	protected void onPause() {
		super.onPause();
		((PersistentTrackerManager) trackerManager).persistTrackers();

	}

	@Override
	protected void onStop() {
		super.onStop();
		((PersistentTrackerManager) trackerManager).persistTrackers();

	}

	@Override
	public void onTrackerChanged(TrackerEvent trackerEvent) {
		((PersistentTrackerManager) trackerManager).persistTrackers();
	}

}
