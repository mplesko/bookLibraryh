package com.logansrings.booklibrary.logging;

import org.apache.log4j.Logger;

import com.logansrings.booklibrary.event.EventObserver;
import com.logansrings.booklibrary.event.IEvent;
import com.logansrings.booklibrary.notification.Notification;
import com.logansrings.booklibrary.notification.NotificationObserver;


public class Log4JDelegate implements EventObserver, NotificationObserver {

	public void execute(IEvent event) {
		Logger logger = Logger.getLogger(event.getSourceClass());
		
		if(event.isError()) {
			logger.error(event.toString());
		} else if(event.isWarning()) {
			logger.warn(event.toString());
		} else if(event.isInfo()) {
			logger.info(event.toString());
		} else if(event.isDebug()) {
			logger.debug(event.toString());
		}		
	}

	@Override
	public void execute(Notification notification) {

		Logger logger;
		if(notification.isDomain()) {
			logger = Logger.getLogger("DOMAIN");
		} else {
			logger = Logger.getLogger("TECHNICAL");
		}

		if(notification.isError()) {
			logger.error(notification.toString());
		} else {
			logger.info(notification.toString());
		}		
	}

}
