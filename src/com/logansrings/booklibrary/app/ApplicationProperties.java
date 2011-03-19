package com.logansrings.booklibrary.app;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

public class ApplicationProperties {

	private static Properties properties = new Properties();
	static {
		loadProperties();
		outputProperties();
	}

	public static String getPersistenceDelegateClassName() {
		return getPersistenceDelegate();
	}

	private static String getPersistenceDelegate() {
		return (String)getProperties().get("persistenceDelegate");
	}

//	public static String getDatabaseURL() {
//		return (String)getProperties().get("databaseURL");
//	}
//	
//	public static String getDatabaseLogin() {
//		return (String)getProperties().get("databaseLogin");
//	}
//	
//	public static String getDatabasePassword() {
//		return (String)getProperties().get("databasePassword");
//	}
	
	public static String[] getEventObserverClassNames() {
		if (getEventObservers() == null) {
			return null;
		} else {
			return getEventObservers().split(",");
		}
	}

	private static String getEventObservers() {
		return (String)getProperties().get("eventObservers");
	}

	public static String[] getNotificationObserverClassNames() {
		if (getNotificationObservers() == null) {
			return null;
		} else {
			return getNotificationObservers().split(",");
		}
	}

	private static String getNotificationObservers() {
		return (String)getProperties().get("notificationObservers");
	}

	private static void loadProperties() {
		InputStream inputStream = getPropertiesFile();
		if (inputStream == null) {
			System.out.println(
			"ApplicationProperties().getPropertiesFile() returned null");
			return;			
		}
		try {
			getProperties().load(inputStream);
		} catch (IOException e) {
			System.out.println(
					"ApplicationProperties().loadProperties() failed " +
					e.getMessage());
		}
	}

	private static InputStream getPropertiesFile() {
		ClassLoader classLoader = ApplicationProperties.class.getClassLoader();
		return classLoader.getResourceAsStream("application.properties");
//		try {
//			return new FileInputStream("C:\\development\\sandbox\\booklibrary\\src\\application.properties");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		}
	}

	private static void outputProperties() {
		Enumeration<?> keys = getProperties().keys();
		while ( keys.hasMoreElements() ) {
			String keyword = ( String ) keys.nextElement();
			String value = ( String ) getProperties().get( keyword );
			System.out.println( keyword + "=[" + value + "]" );
		}
	}

	protected static Properties getProperties() {
		return properties;
	}

	protected static void setProperties(Properties properties) {
		ApplicationProperties.properties = properties;
	}

}