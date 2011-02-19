package com.logansrings.booklibrary.webservice.simple;

import javax.xml.ws.Endpoint;

public class ServiceSimplePublisher {

	public static void main(String[] args) {
		Endpoint.publish("http://127.0.0.1:9876/booklibrary", new ServiceSimpleImpl());
	}
}
