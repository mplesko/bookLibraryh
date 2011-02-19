package com.logansrings.booklibrary.service;

import javax.jws.WebService;

@WebService (endpointInterface = "com.logansrings.booklibrary.service.BookLibraryService")
public class BookLibraryServiceImpl implements BookLibraryService {

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

}
