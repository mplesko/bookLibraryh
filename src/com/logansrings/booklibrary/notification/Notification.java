package com.logansrings.booklibrary.notification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.logansrings.booklibrary.app.ConsolDelegate;
import com.logansrings.booklibrary.logging.LogbackDomainDelegate;
import com.logansrings.booklibrary.logging.LogbackTechnicalDelegate;

public class Notification {

	private static List<NotificationObserver> notificationObservers = 
		new ArrayList<NotificationObserver>();
	
	private Date date;
	private Object sourceObject;
	private String context;
	private String userMessage;
	private String technicalMessage;
	private Exception exception;
	private Type type;
	private Severity severity;

	public static void newNotification(Object sourceObject, String message) {
		newNotification(sourceObject, "", message, "", 
				Type.DOMAIN, Severity.INFO, null);
	}
		
	public static void newNotification(
			Object sourceObject, 
			String context,
			String userMessage, 
			String technicalMessage, 
			Type type, 
			Severity severity) {
		
		newNotification(sourceObject, context, userMessage, technicalMessage, 
				type, severity, null);
	}
		
	public static void newNotification(
			Object sourceObject, 
			String context,
			String userMessage, 
			String technicalMessage, 
			Type type,
			Severity severity, 
			Exception exception) {
		
		Notification notification = new Notification();
		notification.date = new Date();
		notification.sourceObject = sourceObject;
		notification.context = context;
		notification.userMessage = userMessage;
		notification.technicalMessage = technicalMessage;
		notification.type = type;
		notification.severity = severity;
		notification.exception = exception;
		
		for (NotificationObserver notificationObserver : getNotificationObservers()) {
			notificationObserver.execute(notification);
		}
	}
	
	public static void addNotificationObserver(NotificationObserver observer) {
		getNotificationObservers().add(observer);
//		Notification.newNotification(Notification.class,
//				"Notification.addNotificationObserver()",
//				"", observer.toString(), Type.DOMAIN, Severity.INFO);
	}

	static List<NotificationObserver> getNotificationObservers() {
		return notificationObservers;
	}

	static void setNotificationObservers(
			List<NotificationObserver> notificationObservers) {
		Notification.notificationObservers = notificationObservers;
	}

	public boolean isError() {
		return Severity.ERROR == severity;
	}
	
	public boolean isDomain() {
		return Type.DOMAIN == type;
	}

	public boolean isTechnical() {
		return Type.TECHNICAL == type;
	}

	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("** Notification ** ");
		result.append("Date-").append(date);
		result.append(" SourceClass-").append(sourceObject.getClass());
		result.append(" Context-").append(context);
		result.append(" UserMessage-").append(userMessage);
		result.append(" TechnicalMessage-").append(technicalMessage);
		result.append(" Type-").append(type);
		result.append(" Severity-").append(severity);
		if (exception != null) {
			result.append(" Exception-").append(exception.getMessage());
//					ApplicationUtilities.getExceptionAsString(getException()));
		}
		return result.toString();
	}
	
	public static void main(String[] args) {
//		Notification.addNotificationObserver(new Log4JDomainDelegate());
//		Notification.addNotificationObserver(new Log4JTechnicalDelegate());
		
		Notification.addNotificationObserver(new LogbackDomainDelegate());
		Notification.addNotificationObserver(new LogbackTechnicalDelegate());
		Notification.addNotificationObserver(new ConsolDelegate());

		Notification.newNotification(new Object(), "context", "userMessage",
				"add DOMAIN notification", Type.DOMAIN, Severity.INFO);
		Notification.newNotification(new Object(), "context", "userMessage",
				"add TECHNICAL notification", Type.TECHNICAL, Severity.INFO);
	}
}