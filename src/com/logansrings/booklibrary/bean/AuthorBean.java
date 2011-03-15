package com.logansrings.booklibrary.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.model.SelectItem;

import com.logansrings.booklibrary.domain.Author;
import com.logansrings.booklibrary.domain.ObjectFactory;
import com.logansrings.booklibrary.util.SelectItemLabelComparator;
import com.logansrings.booklibrary.webservice.dto.AuthorDto;
public class AuthorBean {

	private Long id;
	private String firstName;
	private String lastName;

	public AuthorBean() {}

	public AuthorBean(Long id, String firstName, String lastName) {
		this(firstName, lastName);
		this.id = id;
	}

	public AuthorBean(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String addAuthor() {
		new Author(firstName, lastName);
		clear();
		return null;
	}

	private void clear() {
		firstName = "";
		lastName = "";
	}

	public String toString() {
		return String.format("[%s] id:%d firstName:%s lastName:%s ",
				"AuthorBean", id, firstName, lastName);
	}

	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public List<AuthorBean> getAuthors() {
		return ObjectFactory.createAuthorBeans(Author.getAll());
	}

	public Author getAuthor() {
		return new Author(id);
	}

	public List<SelectItem> getSortedSelectAuthors() {
		List<SelectItem> selectAuthors = getSelectAuthors();
		Collections.sort(selectAuthors, new SelectItemLabelComparator());
		return selectAuthors;
	}
	public List<SelectItem> getSelectAuthors() {
		List<SelectItem> selectAuthors = new ArrayList<SelectItem>();
		for (AuthorBean authorBean : getAuthors()) {
			selectAuthors.add(new SelectItem(authorBean.id, authorBean.getAuthorLastNameFirstName()));
		}
		return selectAuthors;
	}

	public String getAuthorName() {
		return firstName + " " + lastName;
	}


	public String getAuthorLastNameFirstName() {
		return lastName + ", " + firstName;
	}

	public void setAuthorId(Long id) {
		this.id = id;		
	}

	public Long getAuthorId() {
		return id;
	}

	public AuthorDto getDto() {
		return new AuthorDto(firstName, lastName);
	}

	public List<AuthorDto> getDtos() {
		return ObjectFactory.createAuthorDtos(getAuthors());
	}
}