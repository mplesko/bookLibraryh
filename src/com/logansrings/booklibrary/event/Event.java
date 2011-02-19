package com.logansrings.booklibrary.event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Event implements IEvent {

	private static Event instance = new Event();
	
	private static List<EventObserver> eventObservers = new ArrayList<EventObserver>();
	
	private Date date;
	private Object sourceObject;
	private String context;
	private String userMessage;
	private String technicalMessage;
	private Exception exception;
	private Severity severity;

	protected Event() {}
	
	public static Event getInstance() {
		return instance;
	}
	
	public void newEvent(IEvent sourceEvent) {
		Severity sourceSeverity = Severity.UKNOWN;
		if(sourceEvent.isDebug()) {
			sourceSeverity = Severity.DEBUG;
		} else if(sourceEvent.isInfo()) {
			sourceSeverity = Severity.INFO;
		} else if(sourceEvent.isWarning()) {
			sourceSeverity = Severity.WARNING;
		} else if(sourceEvent.isError()) {
			sourceSeverity = Severity.ERROR;
		}
		newEvent(sourceEvent, sourceEvent.getContext(), sourceEvent
				.getUserMessage(), sourceEvent.getTechnicalMessage(),
				sourceSeverity);
	}
	
	public void newEvent(String context,
			String userMessage, String technicalMessage, Severity severity) {
		
		newEvent(null, context, userMessage, technicalMessage, severity);
	}
		
	public void newEvent(Object sourceObject, String context,
			String userMessage, String technicalMessage, Severity severity) {
		
		newEvent(sourceObject, context, userMessage, technicalMessage, 
				severity, null);
	}
		
	public void newEvent(Object sourceObject, String context,
			String userMessage, String technicalMessage, 
			Severity severity, Exception exception) {
		
		Event event = new Event();
		event.setDate(new Date());
		event.setSourceObject(sourceObject);
		event.setContext(context);
		event.setUserMessage(userMessage);
		event.setTechnicalMessage(technicalMessage);
		event.setSeverity(severity);
		event.setException(exception);
		
		for (EventObserver eventObserver : getEventObservers()) {
			eventObserver.execute(event);
		}
	}
	
	public void addEventObserver(EventObserver observer) {
		getEventObservers().add(observer);
		Event.getInstance().newEvent(Event.class,
				"Event.addEventObserver()",
				"", observer.toString(), Severity.DEBUG);
	}

	public List<EventObserver> getEventObservers() {
		return eventObservers;
	}

	protected void setEventObservers(List<EventObserver> eventObservers) {
		Event.eventObservers = eventObservers;
	}

	public String getContext() {
		return context;
	}

	protected void setContext(String context) {
		this.context = context;
	}

	public Class<? extends Object> getSourceClass() {
		return getSourceObject().getClass();
	}

	public String getTechnicalMessage() {
		return technicalMessage;
	}

	protected void setTechnicalMessage(String technicalMessage) {
		this.technicalMessage = technicalMessage;
	}

	public String getUserMessage() {
		return userMessage;
	}

	protected void setUserMessage(String userMessage) {
		this.userMessage = userMessage;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	public boolean isError() {
		return getSeverity() == Severity.ERROR;
	}

	public boolean isDebug() {
		return getSeverity() == Severity.DEBUG;
	}

	public boolean isInfo() {
		return getSeverity() == Severity.INFO;
	}

	public boolean isWarning() {
		return getSeverity() == Severity.WARNING;
	}

	protected void setSourceObject(Object sourceObject) {
		this.sourceObject = sourceObject;
	}

	protected Object getSourceObject() {
		return sourceObject;
	}
	
	protected void setSeverity(Severity severity) {
		this.severity = severity;
	}

	protected Severity getSeverity() {
		return severity;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDate() {
		return date;
	}

	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("Date-").append(getDate());
		result.append(" SourceClass-").append(getSourceClass());
		result.append(" Context-").append(getContext());
		result.append(" UserMessage-").append(getUserMessage());
		result.append(" TechnicalMessage-").append(getTechnicalMessage());
		result.append(" Severity-").append(getSeverity());
		if (getException() != null) {
			result.append(" Exception-").append(getException().getMessage());
//					ApplicationUtilities.getExceptionAsString(getException()));
		}
		return result.toString();
	}

}
