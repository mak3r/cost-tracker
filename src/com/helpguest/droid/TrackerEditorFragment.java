package com.helpguest.droid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TrackerEditorFragment extends Fragment implements OnPageChangeListener {

	View trackerEditorView = null;
	
	public TrackerEditorFragment() {
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		trackerEditorView = inflater.inflate(R.layout.layout_tracker_editor, container, false);		
        return trackerEditorView;		
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		Log.d(TrackerEditorFragment.class.toString(), "onPageScrollState Changed to '" + arg0 + "'");		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		Log.v(TrackerEditorFragment.class.toString(), "onPageScrolled ... '" + arg0 + "', '" +  arg1 + "', '" +  arg2 + "'");		
	}

	@Override
	public void onPageSelected(int arg0) {
		Log.d(TrackerEditorFragment.class.toString(), "onPageSelected Changed to '" + arg0 + "'");				
	}

}