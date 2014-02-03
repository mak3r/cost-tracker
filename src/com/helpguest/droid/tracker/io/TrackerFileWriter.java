package com.helpguest.droid.tracker.io;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.helpguest.droid.tracker.ITracker;
import com.helpguest.droid.tracker.ITrackerClock;

public class TrackerFileWriter implements ITrackerWriter {

	protected static final char DELIMITER_CHAR_REPLACEMENT = '\007';
	protected static final char DELIMITER_CHAR = ',';
	static final String TRACKER_DATA_FILE = "tracker_data";
	public static final String TRACKER_DATA_V1 = "tracker_data_v1.csv";
	private String dataStore;
	private FileOutputStream fos;
	private Context context;
	
	public TrackerFileWriter(Context applicationContext, SharedPreferences sharedPrefs) {
		dataStore = sharedPrefs.getString(TRACKER_DATA_V1, TRACKER_DATA_V1);
		context = applicationContext;
	}
	
	@Override
	public void write(ITracker tracker) {
		try {
			fos = context.openFileOutput(dataStore, Context.MODE_APPEND);
			fos.write(formatTrackerEntry(tracker).getBytes());
		} catch (FileNotFoundException e) {
			Log.d(TrackerFileWriter.class.toString(), "FileNotFoundException in write() attempting to create it: " + e.getMessage());
		} catch (IOException iox) {
			Log.d(TrackerFileWriter.class.toString(), "IOException in write(): " + iox.getMessage());
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					Log.d(TrackerFileWriter.class.toString(), "Failed closing fos in write(): " + e.getMessage());
				}
			}
		}
	}
	
	public void write(Collection<ITracker> trackers) {
		clear();
		for (ITracker t : trackers) {
			write(t);
		}
	}
	
	public void clear() {
		try {
			fos = context.openFileOutput(dataStore, Context.MODE_PRIVATE);
			fos.write("".getBytes());
		} catch (FileNotFoundException e) {
			Log.i(TrackerFileWriter.class.toString(), "Could not clear file because it was not found.");
		} catch (IOException e) {
			Log.i(TrackerFileWriter.class.toString(), "Could not clear file due to an IOException.");
		} finally {			
			try {
				fos.close();
			} catch (IOException e) {
				Log.i(TrackerFileWriter.class.toString(), "Could not close file after trying to clear it.");
			}
		}
	}

	private String formatTrackerEntry(ITracker tracker) {
		StringBuffer entries = new StringBuffer();
		for(ITrackerClock tClock : tracker.getAllTrackerClocks()) {
			StringBuffer tEntry = new StringBuffer(tracker.getName().replace(DELIMITER_CHAR, DELIMITER_CHAR_REPLACEMENT));
			tEntry.append(",");
			tEntry.append(tracker.getDescription().replace(DELIMITER_CHAR, DELIMITER_CHAR_REPLACEMENT));
			tEntry.append(",");
			tEntry.append(formatTrackerClockEntry(tClock));
			tEntry.append(",");
			tEntry.append(tracker.isVisible() ? "1" : "0");
			tEntry.append(",");
			tEntry.append(tracker.isAutoStop() ? "1" : "0");
			tEntry.append("\n");
			entries.append(tEntry);
		}
		if (entries.length() == 0) {
			entries.append(tracker.getName());
			entries.append(",");
			entries.append(tracker.getDescription());
			entries.append(",,0,0,");
			entries.append(tracker.isVisible() ? "1," : "0,");
			entries.append(tracker.isAutoStop() ? "1" : "0");
			entries.append("\n");			
		}
		return entries.toString();
	}

	private String formatTrackerClockEntry(ITrackerClock tClock) {	
		String note = tClock.getNote();
		StringBuffer tcEntry = new StringBuffer(note == null ? "" : note.replace(DELIMITER_CHAR, DELIMITER_CHAR_REPLACEMENT));
		tcEntry.append(",");
		Long startTime = tClock.startTime();
		tcEntry.append(startTime == null ? "0" : startTime.toString());
		tcEntry.append(",");
		Long stopTime = tClock.stopTime();
		tcEntry.append(stopTime == null ? "0" : stopTime.toString());
		return tcEntry.toString();
	}

}
