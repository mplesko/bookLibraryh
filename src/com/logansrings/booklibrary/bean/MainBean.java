package com.logansrings.booklibrary.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.el.ELContext;
import javax.el.ELResolver; 
import javax.faces.context.FacesContext;

import com.logansrings.booklibrary.domain.Book;
import com.logansrings.booklibrary.domain.Library;
import com.logansrings.booklibrary.domain.ObjectFactory;

/**
 * Backs the main.jsp page
 * @author mark
 */
public class MainBean {
	private UserBean userBean;
	private Library library;
	private List<BookBean> bookBeans;
	private Long addBookId;

	public Long getAddBookId() {
		return addBookId;
	}

	public void setAddBookId(Long addBookId) {
		this.addBookId = addBookId;
	}

	public MainBean() {
		System.out.println("MainBean.constructor");
		initialize();
	}

	private void initialize() {
		initializeUserBean();		
		initializeLibrary();
	}

	private void initializeUserBean() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ELContext    elContext    = facesContext.getELContext();
		ELResolver   elResolver   = elContext.getELResolver();
		userBean = (UserBean)elResolver.getValue(elContext, null, "userbean");
	}

	private void initializeLibrary() {
		library = new Library(userBean.getUser());
		initializeBookBeans();
	}

	private void initializeBookBeans() {
		bookBeans = createBookBeans(library);
	}
	
	private List<BookBean> createBookBeans(Library library) {
		return ObjectFactory.createBookBeans(library);
	}

	public List<BookBean> getBookBeans() {
		if (library == null) {
			initialize();
		}
		return bookBeans;
	}
	
	public String deleteBooks() {
		if (haveBooksToDelete()) {
			// ok, work to do
		} else {
			return null;
		}
		List<BookBean> booksToDelete = new ArrayList<BookBean>();
		for (BookBean book : bookBeans) {
			if (book.isMarkedForDeletion()) {
				booksToDelete.add(book);
			}
		}
		library.updateBooks(null, createBooks(booksToDelete));
		initializeLibrary();
		return null;
	}

	private List<Book> createBooks(List<BookBean> bookBeans) {
		return ObjectFactory.createBooks(bookBeans);
	}

	public String addBookToLibrary() {
		System.out.println("MainBean.addBookToLibrary()");
		Book book = createBook(addBookId);
		List<Book> books = Collections.singletonList(book);
		library.updateBooks(books, null);
		initializeLibrary();
		return "success";
	}
	
	private Book createBook(Long bookId) {
		return ObjectFactory.createBook(bookId);
	}

	public String cancel() {
		System.out.println("MainBean.cancel()");
		return "success";
	}
	
	public boolean getHaveBooksToDelete() {
		System.out.println("MainBean.getHaveBooksToDelete()");
		return bookBeans.size() > 0 && haveBooksToDelete();
	}

	private boolean haveBooksToDelete() {
		for (BookBean bookBean : bookBeans) {
			if (bookBean.isMarkedForDeletion()) {
				return true;
			}
		}
		return false;
	}

	private boolean haveBooksToAdd() {
		for (BookBean bookBean : bookBeans) {
			if (bookBean.isMarkedForAddition()) {
				return true;
			}
		}
		return false;
	}

	public String logout() {
		clear();
		userBean.logout();
		return "logout";
	}
	
	private void clear() {
		library = null;
		bookBeans = null;		
	}
	
	public String doAction() {
		return null;
	}
	
}
