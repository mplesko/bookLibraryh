package com.logansrings.booklibrary.webservice.simple;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.logansrings.booklibrary.webservice.dto.AuthorDto;

@WebService
public interface ServiceSimple {
	@WebMethod int getBookCount();
	@WebMethod int getAuthorBookCount(String authorName);
	@WebMethod List<AuthorDto> getAuthorList();
}
