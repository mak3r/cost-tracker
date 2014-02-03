package com.helpguest.droid.tracker;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.util.Log;

import com.helpguest.droid.tracker.io.ITrackerReader;
import com.helpguest.droid.tracker.io.ITrackerWriter;
import com.helpguest.droid.tracker.io.TrackerFileReader;
import com.helpguest.droid.tracker.io.TrackerFileWriter;

/**
 * Utility methods to manage the Trackers. This utility will
 * provide an interface to the user to manage how, when and where
 * trackers are persisted so that clients don't have to deal with
 * those issues.
 * 
 * @author mabrams
 *
 */
public class PersistentTrackerManager extends InMemoryTrackerManager {

	private SharedPreferences sharedPrefs;
	private Context applicationContext;
	
	public PersistentTrackerManager(Context appContext, SharedPreferences sharedPreferences) {
		super(appContext);
		applicationContext = appContext;
		this.sharedPrefs = sharedPreferences;
		validateDataStore();
		loadTrackers();
	}
	
	private void loadTrackers() {
		ITrackerReader tReader = new TrackerFileReader(applicationContext, sharedPrefs);
		Collection<ITracker> trackers = tReader.getTrackers();
		add(trackers);
	}
		
	private void validateDataStore() {
		ContextWrapper cWrapper = new ContextWrapper(applicationContext);
		boolean exists = false;
		for (String file : cWrapper.fileList()) {
			if (file.equals(TrackerFileWriter.TRACKER_DATA_V1)) {
				exists = true;
				break;
			}
		}

		if (!exists) {
			File dataStoreFile = new File(applicationContext.getFilesDir(), TrackerFileWriter.TRACKER_DATA_V1);			
			try {
				dataStoreFile.createNewFile();
			} catch (IOException e) {
				Log.d(PersistentTrackerManager.class.toString(), "IOException attempting to create the data store: " + e.getMessage());
			}
		}
	}

	public void persistTrackers() {
		ITrackerWriter tWriter = new TrackerFileWriter(applicationContext, sharedPrefs);
		tWriter.write(getTrackers());
	}


}
