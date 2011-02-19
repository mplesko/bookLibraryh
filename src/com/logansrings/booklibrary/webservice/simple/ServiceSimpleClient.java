package com.logansrings.booklibrary.webservice.simple;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Vector;

import javax.xml.namespace.QName;
import javax.xml.ws.Response;

import org.apache.axis.client.Call;

import com.logansrings.booklibrary.webservice.dto.AuthorDto;


public class ServiceSimpleClient {

	/**
	 * @param args
	 * @throws MalformedURLException 
	 */
	public static void main(String[] args) throws MalformedURLException {
//		test1();
//		test3();
		testGetAuthorList();
	}

	private static void testGetAuthorList() throws MalformedURLException {
		System.out.println("testGetAuthorList()");
		URL url = new URL("http://127.0.0.1:9876/booklibrary?wsdl");
		QName qname = new QName("http://simple.webservice.booklibrary.logansrings.com/", "ServiceSimpleImplService");
		javax.xml.ws.Service service = javax.xml.ws.Service.create(url, qname);
		ServiceSimple serviceEndpointInterface = service.getPort(ServiceSimple.class);
		System.out.println(serviceEndpointInterface.getBookCount());
		System.out.println(serviceEndpointInterface.getAuthorBookCount(""));
		List<AuthorDto> dtos = serviceEndpointInterface.getAuthorList();
		for (AuthorDto dto : dtos) {
			System.out.println(dto.getLastNameFirstName());
		}
	}

	private static void test1() throws MalformedURLException {
		System.out.println("test1()");
		URL url = new URL("http://127.0.0.1:9876/booklibrary?wsdl");
		QName qname = new QName("http://simple.webservice.booklibrary.logansrings.com/", "ServiceSimpleImplService");
		javax.xml.ws.Service service = javax.xml.ws.Service.create(url, qname);
		ServiceSimple serviceEndpointInterface = service.getPort(ServiceSimple.class);
		System.out.println(serviceEndpointInterface.getBookCount());
		System.out.println(serviceEndpointInterface.getAuthorBookCount(""));
	}

	private static void test3() {
		System.out.println("test3()");
		try {
//			org.apache.axis.client.Service  service  = new org.apache.axis.client.Service();
//			Call     call     = (Call) service.createCall();
			URL url = new URL("http://127.0.0.1:9876/booklibraryservice");
			
			org.apache.axis.client.Call aCall = new Call(url);
			String ret = (String) aCall.invoke("getBookCount", new Object[]{});
			
//			call.setTargetEndpointAddress(url);
			//			call.setOperationName(new QName("http://soapinterop.org/", echoString"));
			

			Call call = new Call(url);
			ret = (String) call.invoke("getBookCount", new Object[]{});

			System.out.println(ret);
		} catch (Exception e) {
			System.err.println(e.toString());
		}

	}

}
