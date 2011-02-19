package com.logansrings.booklibrary.service;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface BookLibraryService {
	@WebMethod int getBookCount();
	@WebMethod int getAuthorBookCount(String authorName);

}
