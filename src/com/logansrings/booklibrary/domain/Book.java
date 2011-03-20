package com.logansrings.booklibrary.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.servlet.ServletException;

import org.hibernate.annotations.GenericGenerator;

import com.logansrings.booklibrary.app.ApplicationContext;
import com.logansrings.booklibrary.app.ApplicationUtilities;
import com.logansrings.booklibrary.persistence.Persistable;
import com.logansrings.booklibrary.persistence.PersistableComplex;
import com.logansrings.booklibrary.persistence.PersistenceDelegate;

@Entity
@Table( name = "book" )
public class Book implements PersistableComplex {
	@Id
	@Column(name = "id")
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private Long id;
	@Version
    @Column(name="version")
    private Integer version;
	
	@Column(name = "title")
	private String title = "";
	
	@ManyToOne(cascade = {CascadeType.ALL}, fetch=FetchType.EAGER)
    @JoinColumn(name="authorid")
	private Author author;
	
	@Transient
	private Long authorId;
	@Transient
	private boolean valid;
	@Transient
	private String context = "";

	public static void main(String[] args) throws ServletException {
//		Book book = new Book("Mythical Man Month", Author.getTestAuthor());
//		System.out.println(Book.getTestBook().toString());
		Book.getAll();
	}	

	Book() {}
	
	private Book(Long bookId) {
		this.id = bookId;
		valid = true;
	}

	private Book(String title) {
		this.title = title;
		if (ApplicationUtilities.isEmpty(title)) {
			valid = false;
			context = "must have title";
		}  else {
			valid = true;
		}
	}

	private Book(String title, Author author) {
		this.title = title;
		this.author = author;		
		if (ApplicationUtilities.isEmpty(title)) {
			valid = false;
			context = "must have title";
		} else {
			if (author == null) {
				valid = false;
				context = "must have author";
			} else {
				valid = true;
			}
		}
	}

	public static Book find(String title, Author author) {
		Book book = new Book(title, author);
		if (book.isNotValid()) {
			return book;
		}
		Persistable persistable = getPersistenceDelegate().findOne(book);
		if (persistable == null) {
			book.valid = false;
			return book;
		}
		book = (Book)persistable;
		book.valid = true;
		return book;
	}

	public static Book find(Long bookId) {
		Book book = new Book(bookId);
		Persistable persistable = getPersistenceDelegate().findById(book);
		if (persistable == null) {
			book.valid = false;
			return book;
		}
		book = (Book)persistable;
		book.valid = true;
		return book;
	}

	public static Book create(String title, Long authorId) {
		Book book = new Book(title);
		if (book.isNotValid()) {
			return book;
		}
		if (getPersistenceDelegate().exists(book)) {
			book.valid = false;
			book.context = "already exists";
			return book;
		}
		book.authorId = authorId;
		if (getPersistenceDelegate().persist(book, new Author(authorId))) {
			book.valid = true;
		} else {
			book.valid = false;
			book.context = "unable to persist book";
		}
		return book;
	}
	
	public static Book create(String title, Author author) {
		Book book = new Book(title, author);
		if (book.isNotValid()) {
			return book;
		}
		if (getPersistenceDelegate().exists(book)) {
			book.valid = false;
			book.context = "already exists";
			return book;
		}
		if (getPersistenceDelegate().persist(book)) {
			book.valid = true;
		} else {
			book.valid = false;
			book.context = "unable to persist book";
		}
		return book;
	}
	
	public String getToString() {
		return toString();
	}
	public String toString() {
		return String.format("[%s] id:%d title:%s valid:%s context:%s  version:%d author:%s",
				"Book", id, title, valid, context, version, author.toString());
	}

	public static Book getTestBook() {
		Book book = new Book();
		book.id = 1L;
		book.title = "bookTitle";
		book.author = Author.getTestAuthor();		
		book.valid = true;
		book.context = "a context";
		book.version = 1;
		return book;
	}

	public static Book getTestBook(String title, Author author) {
		Book book = new Book();
		book.title = title;
		book.author = author;		
		book.valid = true;
		book.context = "a context";
		book.version = 1;
		return book;
	}

	static private PersistenceDelegate getPersistenceDelegate() {
		return ApplicationContext.getPersistenceDelegate();
	}

	String getTitle() {return title;}
	
	public Long getId() {return id;}
	@Override
	public void setId(Long id) {this.id = id;}

	Long getAuthorId() {return authorId;}

	String getAuthorFirstName() {return author.getFirstName();}
	String getAuthorLastName() {return author.getLastName();}
	
	public boolean isValid() {return valid;}
	public boolean isNotValid() {return ! isValid();}

	public static List<Book> getAll() {
		List<Book> books = new ArrayList<Book>();
		List<Persistable> findList = getPersistenceDelegate().findAll(new Book());
		if (findList.size() == 0) {
			// nothing to do
		} else {
			for (Persistable persistable : findList) {
				books.add((Book)persistable);
			}
		}
		return books;
	}

	@Override
	public Integer getVersion() {return version;}
	@Override
	public void setVersion(Integer version) {this.version = version;}
	
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Book)) {
			return false;
		}		
		final Book that = (Book)other;
		return this == that ||
				(this.title.equals(that.title) && 
						this.author.equals(that.author));
	}
	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + (null == title ? 0 : title.hashCode());
		hash = 31 * hash + (null == author ? 0 : author.hashCode());
		return hash;
	}

	public void setAssociatedPersistable(Persistable associatedPersistable) {
		author = (Author)associatedPersistable;		
	}

}
