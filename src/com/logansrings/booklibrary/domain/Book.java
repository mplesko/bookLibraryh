package com.logansrings.booklibrary.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import com.logansrings.booklibrary.persistence.PersistenceDelegate;

@Entity
@Table( name = "book" )
public class Book implements Persistable{
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
	
	@ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name="authorid")
	private Author author;
	
	@Transient
	private Long authorId;
	@Transient
	private String persistableMode;
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
	
	public Book(String title, Author author) {
		this.title = title;
		this.author = author;		
		if (ApplicationUtilities.isEmpty(title)) {
			valid = false;
			context = "must have title";
		} else {
			findByTitle();
			if (isNotValid()) {
				persistBook();
			}
		}
	}

	public Book(Long bookId) {
		this.id = bookId;
		findById();
		author = new Author(authorId);
	}

	private void findById() {
		persistableMode = "findId";
		Persistable persistable = getPersistenceDelegate().findOne(this);
		if (persistable == null) {
			title = "N/A";
			valid = false;
		} else {
			valid = true;
			title = ((Book)persistable).getTitle();
			authorId = ((Book)persistable).getAuthorId();
		}
	}

	private void findByTitle() {
		persistableMode = "findTitle";
		Persistable persistable = getPersistenceDelegate().findOne(this);
		if (persistable == null) {
			valid = false;
		} else {
			valid = true;
			id = persistable.getId();
		}
	}

	public void persistBook() {
		persistableMode = "create";
		if (getPersistenceDelegate().persist(this)) {
			// ok, expected
		} else {
			valid = false;
			context = "unable to persist book";
		}
	}
	
	public String getToString() {
		return toString();
	}
	public String toString() {
		return String.format("[%s] id:%d title:%s valid:%s context:%s author:%s",
				"Book", id, title, valid, context, author.toString());
	}

	public static Book getTestBook() {
		Book book = new Book();
		book.id = 1L;
		book.title = "bookTitle";
		book.author = Author.getTestAuthor();		
		book.valid = true;
		book.context = "a context";
		return book;
	}

	public int getColumnCount() {
		return 3;
	}

	public String[] getColumnNames() {
		if ("findId".equals(persistableMode)) {
			return new String[]{"id"};
		} else if ("findTitle".equals(persistableMode)) {
			return new String[]{"title"};
		} else {
			return new String[]{"id", "title", "authorId"};
		}
	}

	public Object[] getColumnValues() {
		if ("findId".equals(persistableMode)) {
			return new Object[]{id};
		} else if ("findTitle".equals(persistableMode)) {
			return new Object[]{title};
		} else {
			return new Object[]{id, title, author.getId()};
		}
	}

	public String getTableName() {
		return "book";
	}

	static private PersistenceDelegate getPersistenceDelegate() {
		return ApplicationContext.getPersistenceDelegate();
	}

	String getTitle() {
		return title; 
	}
	
	public Long getId() {
		return id; 
	}

	Long getAuthorId() {
		return authorId; 
	}

	String getAuthorFirstName() {
		return author.getFirstName();
	}
	String getAuthorLastName() {
		return author.getLastName();
	}
	
	public boolean isValid() {
		return valid;
	}

	public boolean isNotValid() {
		return ! isValid();
	}

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
	public Persistable newFromDBColumns(List<Object> objectList) {
		Book book = new Book();
		if (objectList.size() == 3) {
			book.id = (Long) objectList.get(0);
			book.title = (String) objectList.get(1);
			book.authorId = (Long) objectList.get(2);
			book.author = new Author(book.authorId);
		}
		return book;
	}
	@Override
	public void setId(Long id) {
		this.id = id;		
	}


	@Override
	public Integer getVersion() {
		return version;
	}


	@Override
	public void setVersion(Integer version) {
		this.version = version;
		
	}
}
