package com.logansrings.booklibrary.logging;

import org.slf4j.LoggerFactory;

import com.logansrings.booklibrary.notification.Notification;
import com.logansrings.booklibrary.notification.NotificationObserver;

public class LogbackDomainDelegate implements NotificationObserver {

	@Override
	public void execute(Notification notification) {
		if (notification.isDomain()) {
			if(notification.isError()) {
				LoggerFactory.getLogger("DOMAIN").error(notification.toString());
			} else {
				LoggerFactory.getLogger("DOMAIN").info(notification.toString());
			}
		}
	}
}
