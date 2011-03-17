package com.logansrings.booklibrary.domain;

import java.util.ArrayList;
import java.util.List;

import com.logansrings.booklibrary.app.ApplicationContext;
import com.logansrings.booklibrary.app.ApplicationUtilities;
import com.logansrings.booklibrary.persistence.Persistable;
import com.logansrings.booklibrary.persistence.PersistenceDelegate;

/**
 * @author mark
 * @deprecated
 */
public class Library {

	private User user;
	private List<Book> books = new ArrayList<Book>();

	public static void main(String[] args) {
		//		User user = new User("x", "x", UserType.EXISTING);
		//		Library library = new Library(user);
	}

	public Library(User user) {
		this.user = user;
		initializeBooks();
	}

	private void initializeBooks() {
		UserBook userBook = new UserBook(user, "find");
		List<Persistable> findList = getPersistenceDelegate().findAny(userBook);
		if (findList.size() == 0) {
			return;
		} else {
			for (Persistable persistable : findList) {
				Long bookId = persistable.getId();
				books.add(Book.find(bookId));
			}
		}
	}

	/**
	 * Adds and/or deletes books from this library.
	 * @param booksToAdd list of books to add or null if no books to add
	 * @param booksToDelete list of books to delete or null if no books to delete
	 */
	public void updateBooks(List<Book> booksToAdd, List<Book> booksToDelete) {
		System.out.println("Library.updateBooks()");
		if (ApplicationUtilities.isEmpty(booksToAdd)) {
			// nothing to do
		} else {
			addBooks(booksToAdd);
		}
		if (ApplicationUtilities.isEmpty(booksToDelete)) {
			// nothing to do
		} else {
			deleteBooks(booksToDelete);
		}
	}

	private void deleteBooks(List<Book> booksToDelete) {
		for (Book book : booksToDelete) {
			UserBook userBook = new UserBook(user, book, "delete");
			// TODO deal with failure
			boolean successful = getPersistenceDelegate().delete(userBook);
		}		
	}

	private void addBooks(List<Book> booksToAdd) {
		for (Book book : booksToAdd) {
			// TODO deal with failure
			boolean successful = addBook(book);
		}		
	}

	private boolean addBook(Book book) {
		UserBook userBook = new UserBook(user, book, "create");
		return getPersistenceDelegate().persist(userBook);
	}
	public String toString() {
		StringBuilder libraryString = new StringBuilder();
		libraryString.append(user.toString());
		libraryString.append(" ");
		for (Book book : books) {
			libraryString.append(book.toString());
			libraryString.append(" ");
		}
		return libraryString.toString();
	}

	static private PersistenceDelegate getPersistenceDelegate() {
		return ApplicationContext.getPersistenceDelegate();
	}

	List<Book> getBooks() {
		return books;
	}

	class UserBook implements Persistable {
		private String persistableMode;
		private User user;
		private Book book;

		public UserBook(User user, String mode) {
			this(user, null, mode);
		}

		public UserBook(User user, Book book, String mode) {
			this.user = user;
			this.book = book;
			this.persistableMode = mode;
		}

//		@Override
		public int getColumnCount() {
			return 2;
		}

//		@Override
		public String[] getColumnNames() {
			if ("find".equals(persistableMode)) {
				return new String[] {"userId"};
			} else {
				return new String[] {"userId", "bookId"};
			}
		}

//		@Override
		public Long[] getColumnValues() {
			if ("find".equals(persistableMode)) {
				return new Long[] {user.getId()};
			} else {
				return new Long[] {user.getId(), book.getId()};
			}
		}

//		@Override
		public String getTableName() {
			return "userbook";
		}	

		public String toString() {
			return String.format("[%s] user:%s book:%s",
					"Library", user.toString(), book.toString());
		}

		@Override
		public Long getId() {
			// TODO Auto-generated method stub
			return null;
		}

//		@Override
		public Persistable newFromDBColumns(List<Object> objectList) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setId(Long id) {
			//			this.id = id;		
		}

		@Override
		public boolean isValid() {
			return true;
		}

		@Override
		public Integer getVersion() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setVersion(Integer version) {
			// TODO Auto-generated method stub
			
		}
	}

}