package com.logansrings.booklibrary.event;

public interface IEvent {

	Class getSourceClass();
	String getContext();
	String getUserMessage();
	String getTechnicalMessage();
	boolean isDebug();
	boolean isInfo();
	boolean isWarning();
	boolean isError();
}
