package com.logansrings.booklibrary.app;

import com.logansrings.booklibrary.event.EventObserver;
import com.logansrings.booklibrary.event.IEvent;
import com.logansrings.booklibrary.notification.Notification;
import com.logansrings.booklibrary.notification.NotificationObserver;

public class ConsolDelegate implements EventObserver, NotificationObserver {

	public void execute(IEvent event) {
		System.out.println(event.toString());		
	}

	@Override
	public void execute(Notification notification) {
		System.out.println(notification.toString());
	}

}
