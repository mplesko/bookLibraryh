package com.logansrings.booklibrary.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.logansrings.booklibrary.app.ApplicationUtilities;
import com.logansrings.booklibrary.domain.ObjectFactory;
import com.logansrings.booklibrary.domain.User;
import com.logansrings.booklibrary.notification.Notification;
import com.logansrings.booklibrary.notification.Severity;
import com.logansrings.booklibrary.notification.Type;

/**
 * Main controller for the user and user's books - the "library"
 * @author mark
 */
public class UserBean {
	private String userName = "";
	private String password = "";
	private User loggedInUser;
	private List<BookBean> bookBeans;
	private Long addBookId;

	public String createAccount() {
		if (incomplete()) {
			ApplicationUtilities.createFacesError("loginForm",
					"User Name and Password are required", "");
			return "failure";
		}
		try {
			User user = User.create(userName, password);
			if (validUser(user)) {
				loggedInUser = user;
				initializeBookBeans();
				return "success";
			} else {
				ApplicationUtilities.createFacesError("loginForm",
						"Unable to create account:",
						user.getContext());
				return "failure";
			}

		} catch (Exception e) {
			Notification.newNotification(this, "UserBean.createAccount()",
					"failed", "", Type.TECHNICAL, Severity.ERROR, e);
			ApplicationUtilities.createFacesError("loginForm",
					"Technical error prevented user creation", "");
			return "failure";
		}
	}

	public String login() {
		if (incomplete()) {
			ApplicationUtilities.createFacesError("loginForm",
					"User Name and Password are required", "");
			return "failure";
		}
		try {
			User user = User.find(userName, password);
			if (validUser(user)) {
				loggedInUser = user;
				initializeBookBeans();
				return "success";
			} else {
				ApplicationUtilities.createFacesError("loginForm",
						"Unable to login:", user.getContext());
				return "failure";
			}
		} catch (Exception e) {
			Notification.newNotification(this, "UserBean.login()",
					"failed", "", Type.TECHNICAL, Severity.ERROR,
					e);
			ApplicationUtilities.createFacesError("loginForm",
					"Technical error prevented user login", "");
			return "failure";
		}
	}

	boolean validUser(User user) {
		return user.isValid();
	}

	public String logout() {
		clear();
		return "logout";
	}

	private void clear() {
		userName = "";
		password = "";
		loggedInUser = null;
		bookBeans = Collections.emptyList();
	}

	private boolean incomplete() {
		return ApplicationUtilities.isEmpty(userName, password);
	}

	public String getUserName() { return userName; }
	public void setUserName(String newValue) { userName = newValue; }

	public String getPassword() { return password; }
	public void setPassword(String newValue) { password = newValue; }

	public User getUser() {
		return loggedInUser;
	}

	public boolean isLoggedIn() {
		return loggedInUser != null;
	}

	public boolean isNotLoggedIn() {
		return ! isLoggedIn();
	}

//	public Collection<Book> getBooks() {
//		return loggedInUser.getBooks();
//	}
	private void initializeBookBeans() {
		bookBeans = ObjectFactory.createBookBeans(loggedInUser.getBooks());
	}
	
	public List<BookBean> getBookBeans() {
		return bookBeans;
	}
	
	public boolean getHaveBooksToDelete() {
		return bookBeans.size() > 0 && haveBooksToDelete();
	}
	private boolean haveBooksToDelete() {
		return ! getBookIdsToDelete().isEmpty();
	}
	private List<Long> getBookIdsToDelete() {
		List<Long> idsToDelete = new ArrayList<Long>();
		for (BookBean book : bookBeans) {
			if (book.isMarkedForDeletion()) {
				idsToDelete.add(book.getId());
			}
		}
		return idsToDelete;
	}

	public String addBookToLibrary() {
		loggedInUser.addBook(addBookId);
		initializeBookBeans();
		return "success";
	}

	public void deleteBooks() {
		loggedInUser.deleteBooks(getBookIdsToDelete());
		initializeBookBeans();
	}

	public void setAddBookId(Long addBookId) {
		this.addBookId = addBookId;
	}
	public Long getAddBookId() {
		return addBookId;
	}
}				