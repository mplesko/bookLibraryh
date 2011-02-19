package com.logansrings.booklibrary.webservice.simple;

import java.util.List;

import javax.jws.WebService;

import com.logansrings.booklibrary.bean.AuthorBean;
import com.logansrings.booklibrary.webservice.dto.AuthorDto;

@WebService (endpointInterface = "com.logansrings.booklibrary.webservice.simple.ServiceSimple")
public class ServiceSimpleImpl implements ServiceSimple {

	@Override
	public int getBookCount() {
		System.out.println("BookLibraryServiceImpl.getBookCount()");
		return 1234;
	}

	@Override
	public int getAuthorBookCount(String authorName) {
		System.out.println("BookLibraryServiceImpl.getAuthorBookCount()");
		return 5678;
	}

	@Override
	public List<AuthorDto> getAuthorList() {
		return new AuthorBean().getDtos();
	}

}
