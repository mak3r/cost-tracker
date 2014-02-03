package com.helpguest.droid.tracker.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.helpguest.droid.tracker.ITracker;
import com.helpguest.droid.tracker.SimpleTracker;
import com.helpguest.droid.tracker.TrackerClock;

public class TrackerFileReader implements ITrackerReader {

	private String dataStore;
	private FileInputStream fis = null;
	private Context context;
	
	public TrackerFileReader(Context applicationContext, SharedPreferences sharedPrefs) {
		context = applicationContext;
		dataStore = sharedPrefs.getString(TrackerFileWriter.TRACKER_DATA_V1, TrackerFileWriter.TRACKER_DATA_V1);
	}
	
	@Override
	public Collection<ITracker> getTrackers() {
		Map<String, ITracker> trackers = new HashMap<String, ITracker>();
		try {
			read(trackers);
		} catch (FileNotFoundException fnfx) {
			Log.d(TrackerFileReader.class.toString(), "FileNotFoundException in getTrackers(): " + fnfx.getMessage());
		} catch (IOException ioxe) {
			Log.d(TrackerFileReader.class.toString(), "IOException in getTrackers(): " + ioxe.getMessage());
		} finally {
			try {
				fis.close();
			} catch (IOException iox) {
				Log.d(TrackerFileReader.class.toString(), "IOException closing file in getTrackers(): " + iox.getMessage());
			}
		}

		return trackers.values();
	}

	private void read(Map<String, ITracker> trackers)
			throws FileNotFoundException, IOException {
		fis = context.openFileInput(dataStore);
		LineNumberReader lnr = new LineNumberReader(new InputStreamReader(fis));
		String nextLine = null;
		int lineNumber = 0;
		while( (nextLine = lnr.readLine()) != null) {
			ITracker nextTracker  = extractTracker(nextLine, lineNumber++);
			if (nextTracker != null) {
				ITracker existingTracker = trackers.get(nextTracker.getName());
				if (existingTracker != null) {
					((SimpleTracker)existingTracker).add(nextTracker.getAllTrackerClocks());
				} else {
					trackers.put(nextTracker.getName(), nextTracker);						
				}					
			}
		}
	}

	private ITracker extractTracker(String nextLine, int lineNumber) {
		ITracker tracker = null;
		String[] data = nextLine.split(String.valueOf(TrackerFileWriter.DELIMITER_CHAR));
		try {
			tracker = new SimpleTracker(data[0].replace(TrackerFileWriter.DELIMITER_CHAR_REPLACEMENT, TrackerFileWriter.DELIMITER_CHAR));
			tracker.setDescription(data[1] == null ? "" : data[1].replace(TrackerFileWriter.DELIMITER_CHAR_REPLACEMENT, TrackerFileWriter.DELIMITER_CHAR));
			TrackerClock tClock = new TrackerClock(data[3] == null ? 0 : Long.parseLong(data[3]), data[4] == null ? 0 : Long.parseLong(data[4]));
			tClock.setNote(data[2] == null ? "" : data[2].replace(TrackerFileWriter.DELIMITER_CHAR_REPLACEMENT, TrackerFileWriter.DELIMITER_CHAR));
			tracker.add(tClock);
			tracker.setVisible(data[5] == null || Integer.parseInt(data[5]) > 0 ? true : false);
			//doing this last since the original data format doesn't have index 6 - 
			// after the file rewrites itself, this will not be an issue.
			tracker.setAutoStop(data[6] == null || Integer.parseInt(data[6]) > 0 ? true : false);
		} catch( ArrayIndexOutOfBoundsException aex) {
			Log.e(TrackerFileReader.class.toString(), "Error reading line: " + lineNumber + ": '" + nextLine + "'" + aex.getMessage());
		} catch( NumberFormatException nfex ) {
			Log.e(TrackerFileReader.class.toString(), "Error parsing 'visible' flag: " + lineNumber + ": '" + nextLine + "'" + nfex.getMessage());
		}
		return tracker;
	}

}
