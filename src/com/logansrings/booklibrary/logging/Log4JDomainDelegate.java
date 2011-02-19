package com.logansrings.booklibrary.logging;

import org.apache.log4j.Logger;

import com.logansrings.booklibrary.notification.Notification;
import com.logansrings.booklibrary.notification.NotificationObserver;

public class Log4JDomainDelegate implements NotificationObserver {

	@Override
	public void execute(Notification notification) {
		if (notification.isDomain()) {
			if(notification.isError()) {
				Logger.getLogger("DOMAIN").error(notification.toString());
			} else {
				Logger.getLogger("DOMAIN").info(notification.toString());
			}
		}
	}
}
