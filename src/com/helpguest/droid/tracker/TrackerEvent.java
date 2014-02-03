package com.helpguest.droid.tracker;

public class TrackerEvent {

	public enum Type {CREATED, UPDATED, MODIFIED, DELETED};
	private String message;
	private Type eventType;
	
	public TrackerEvent(String message, Type type) {
		this.message = message;
		this.eventType = type;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	public Type getEventType() {
		return eventType;
	}

	public void setEventType(Type eventType) {
		this.eventType = eventType;
	}
	
}
