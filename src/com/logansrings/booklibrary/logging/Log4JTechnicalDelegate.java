package com.logansrings.booklibrary.logging;

import org.apache.log4j.Logger;

import com.logansrings.booklibrary.notification.Notification;
import com.logansrings.booklibrary.notification.NotificationObserver;

public class Log4JTechnicalDelegate implements NotificationObserver {

	@Override
	public void execute(Notification notification) {
		if (notification.isTechnical()) {
			if(notification.isError()) {
				Logger.getLogger("TECHNICAL").error(notification.toString());
			} else {
				Logger.getLogger("TECHNICAL").info(notification.toString());
			}
		}
	}
}
