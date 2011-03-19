package com.logansrings.booklibrary.app;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.logansrings.booklibrary.notification.Notification;
import com.logansrings.booklibrary.notification.NotificationObserver;
import com.logansrings.booklibrary.persistence.PersistenceDelegate;


/**
 * Sets up and/or holds on to the application context.
 * Currently responsible for the persistenceDelegate and the 
 * list of eventObservers.
 * 
 * @author mark
 *
 */
@SuppressWarnings("serial")
public class ApplicationContext extends HttpServlet {

	private static PersistenceDelegate persistenceDelegate;

	/**
	 * Initializes the application context
	 */
	public void init() throws ServletException {
		initNotificationObservers();
		initPersistenceDelegate();
	}

	private static void initNotificationObservers() {
		String[] notificationObserverClassNames = 
			ApplicationProperties.getNotificationObserverClassNames();

		if (notificationObserverClassNames == null) {
			// nothing to do
		} else {
			for (String notificationObserverClassName : notificationObserverClassNames) {
				if (ApplicationUtilities.isNotEmpty(notificationObserverClassNames)) {
					Notification.addNotificationObserver(
							(NotificationObserver)
							ApplicationUtilities.createObjectForClassName(
									notificationObserverClassName));
				}
			}
		}
	}

	private static void initPersistenceDelegate() {
		if (ApplicationProperties.getPersistenceDelegateClassName() == null) {
			System.out.println(
					"ApplicationProperties().getPersistenceDelegateClassName() returned null");
		} else {
			setPersistenceDelegate(
					(PersistenceDelegate)ApplicationUtilities.createObjectForClassName(
							ApplicationProperties.getPersistenceDelegateClassName()));
		}
	}

	protected static void setPersistenceDelegate(PersistenceDelegate persistenceDelegate) {
		ApplicationContext.persistenceDelegate = persistenceDelegate;
		Notification.newNotification(new ApplicationContext(),
				"ApplicationContext.setPersistenceDelegate() " +
				persistenceDelegate.toString());
	}

	public static PersistenceDelegate getPersistenceDelegate() {
		if (persistenceDelegate == null) {
			initPersistenceDelegate();
		}
		return persistenceDelegate;
	}
}
