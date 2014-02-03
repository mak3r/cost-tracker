package com.helpguest.droid.tracker;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TrackerClockTester {

	ITrackerClock tc = new TrackerClock();
	
	@Before
	public void setUp() throws Exception {
		tc.start(null);
	}

	@After
	public void tearDown() throws Exception {
		
	}

	@Test
	public void testExpired() {
		assertFalse(tc.isExpired());
		tc.stop();
		assertTrue(tc.isExpired());
	}

	@Test
	public void testIsStarted() {
		assertTrue(tc.isStarted());
		tc.stop();
		//expect true even after we stop the ITrackerClock.
		assertTrue(tc.isStarted());
	}

	@Test
	public void testDuration() {
		assertTrue(tc.duration() == 0);
		tc.stop();
		assertTrue(tc.duration() > 0);
	}

	@Test(expected = UnsupportedOperationException.class)	
	public void testReStart() {
		assertNotNull(tc.start(null));
		tc.start(null);
	}

}
