package com.logansrings.booklibrary.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.el.ELContext;
import javax.el.ELResolver; 
import javax.faces.context.FacesContext;

import com.logansrings.booklibrary.domain.ObjectFactory;

/**
 * Main controller for the user and user's books - the "library"
 * @author mark
 */
public class MainBean {
	private UserBean userBean;
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
		initializeBookBeans();
	}

	private void initializeUserBean() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ELContext    elContext    = facesContext.getELContext();
		ELResolver   elResolver   = elContext.getELResolver();
		userBean = (UserBean)elResolver.getValue(elContext, null, "userbean");
	}

	private void initializeBookBeans() {
		bookBeans = ObjectFactory.createBookBeans(userBean.getBooks());
	}
	
	public List<BookBean> getBookBeans() {
		return bookBeans;
	}
	
	public String deleteBooksFromLibrary() {
		if (haveBooksToDelete()) {
			// ok, work to do
		} else {
			return null;
		}
		List<Long> booksToDelete = new ArrayList<Long>();
		for (BookBean bookBean : bookBeans) {
			if (bookBean.isMarkedForDeletion()) {
				booksToDelete.add(bookBean.getId());
			}
		}
		userBean.deleteBooks(booksToDelete);
		initializeBookBeans();
		return null;
	}

	public String addBookToLibrary() {
		System.out.println("MainBean.addBookToLibrary()");
		userBean.addBook(addBookId);
		initializeBookBeans();
		return "success";
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

	public String logout() {
		clear();
		userBean.logout();
		return "logout";
	}
	
	private void clear() {		
		bookBeans  = Collections.emptyList();		
	}
	
	public String doAction() {
		return null;
	}	
}
