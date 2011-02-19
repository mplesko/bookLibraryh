package com.logansrings.booklibrary.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.model.SelectItem;

import com.logansrings.booklibrary.domain.Author;
import com.logansrings.booklibrary.domain.Book;
import com.logansrings.booklibrary.domain.ObjectFactory;
import com.logansrings.booklibrary.util.SelectItemLabelComparator;

public class BookBean {
	private Long id;
	private String title;
	private AuthorBean authorBean;
	private boolean markedForDeletion;
	private boolean markedForAddition;

	public BookBean() {
		this("", "", "");
	}

	public BookBean(String title, String authorFirstName, String authorLastName) {
		this(null, title, null, authorFirstName, authorLastName);
		markedForAddition = true;
	}

	public BookBean(
			Long id, String title, Long authorId, String authorFirstName, String authorLastName) {
		this.id = id;
		this.title = title;
		authorBean = new AuthorBean(authorId, authorFirstName, authorLastName);
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public String getAuthorName() {
		return authorBean.getAuthorName();
	}
	public String getAuthorFirstName() {
		return authorBean.getFirstName();
	}

	public String getAuthorLastName() {
		return authorBean.getLastName();
	}

	public boolean isMarkedForDeletion() {
		return markedForDeletion;
	}

	public void setMarkedForDeletion(boolean markedForDeletion) {
		this.markedForDeletion = markedForDeletion;
	}

	public boolean isMarkedForAddition() {
		return markedForAddition;
	}

	public void setMarkedForAddition(boolean markedForAddition) {
		this.markedForAddition = markedForAddition;
	}

	public List<BookBean> getBooks() {
		return ObjectFactory.createBookBeans(Book.getAll());
	}

	public String addBook() {
		System.out.println("In BookBean.addBook()");
		ObjectFactory.createBook(title, getAuthorId());
//		new Book(title, authorBean.getAuthor());
		clear();
		return null;
	}
	
	private void clear() {
		title = "";
	}

	public void setAuthorId(Long id) {
		authorBean.setAuthorId((Long) id);
		System.out.println("In BookBean.setAuthorId()");
	}
	public Long getAuthorId() {
		return authorBean.getAuthorId();
	}


	public List<SelectItem> getSortedSelectBooks() {
		List<SelectItem> selectBooks = getSelectBooks();
		Collections.sort(selectBooks, new SelectItemLabelComparator());
		return selectBooks;
	}
	public List<SelectItem> getSelectBooks() {
		List<SelectItem> selectBooks = new ArrayList<SelectItem>();
		for (BookBean bookBean : getBooks()) {
			selectBooks.add(new SelectItem(bookBean.id, bookBean.getTitle()));
		}
		return selectBooks;
	}


}
