package com.logansrings.booklibrary.logging;

import org.slf4j.LoggerFactory;

import com.logansrings.booklibrary.notification.Notification;
import com.logansrings.booklibrary.notification.NotificationObserver;

public class LogbackTechnicalDelegate implements NotificationObserver {

	@Override
	public void execute(Notification notification) {
		if (notification.isTechnical()) {
			if(notification.isError()) {
				LoggerFactory.getLogger("TECHNICAL").error(notification.toString());
			} else {
				LoggerFactory.getLogger("TECHNICAL").info(notification.toString());
			}
		}
	}
}
