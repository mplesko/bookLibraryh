package com.logansrings.booklibrary.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.servlet.ServletException;
import org.hibernate.annotations.GenericGenerator;
import com.logansrings.booklibrary.app.ApplicationContext;
import com.logansrings.booklibrary.app.ApplicationUtilities;
import com.logansrings.booklibrary.persistence.Persistable;
import com.logansrings.booklibrary.persistence.PersistenceDelegate;

@Entity
@Table( name = "author" )
public class Author implements Persistable {
	@Id
	@Column(name = "id")
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private Long id;
	@Column(name = "firstName")
	private String firstName;
	@Column(name = "lastName")
	private String lastName;

	@Transient
	private String persistableMode;
	@Transient
	private boolean valid;
	@Transient
	private String context = "";
	@Transient
	private static final String UNINITIALIZED = "must have firstName and lastName";
	
	public static void main(String[] args) throws ServletException {
		new ApplicationContext().init();
		Author author = new Author("Fred", "Brooks");
//		author.persistAuthor();
//		System.out.println(Author.getTestAuthor().toString());
		author.firstName = "";
		author.findById();
	}
	
	Author() {
		valid = false;
		context = UNINITIALIZED;
	}
	
	public Author(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
		if (ApplicationUtilities.isEmpty(firstName, lastName)) {
			valid = false;
			context = UNINITIALIZED;
		} else {
			findByName();
			if (isNotValid()) {
				persistAuthor();
			}
		}
	}
	
	public Author(Long authorId) {
		this.id = authorId;
		findById();
	}

	private void findById() {
		persistableMode = "findId";
		Persistable persistable = getPersistenceDelegate().findById(this);
		if (persistable == null) {
			valid = false;
		} else {
			valid = true;
			firstName = ((Author)persistable).getFirstName();
			lastName = ((Author)persistable).getLastName();
		}
	}

	private void findByName() {
		persistableMode = "findName";
		Persistable persistable = getPersistenceDelegate().findOne(this);
		if (persistable == null) {
			valid = false;
		} else {
			valid = true;
			id = persistable.getId();
		}
	}
	
	public void persistAuthor() {
		persistableMode = "create";
		if (getPersistenceDelegate().persist(this)) {
			// ok, expected
		} else {
			valid = false;
			context = "unable to persist author";
		}
	}
	
	public String toString() {
		return String.format("[%s] id:%d firstName:%s lastName:%s valid:%s context:%s ",
				"Author", id, firstName, lastName, valid, context);
	}

	public static Author getTestAuthor() {
		Author author = new Author();
		author.id = 1L;
		author.firstName = "authorFirstName";
		author.lastName = "authorLastName";
		author.valid = true;
		author.context = "a context";
		return author;
	}
	public static Author getTestAuthor(String firstName, String lastName) {
		Author author = new Author();
		author.firstName = firstName;
		author.lastName = lastName;
		author.valid = true;
		author.context = "a context";
		return author;
	}

	public int getColumnCount() {
		return 3;
	}

	public String[] getColumnNames() {
		if ("findId".equals(persistableMode)) {
			return new String[]{"id"};
		} else if ("findName".equals(persistableMode)) {
			return new String[]{"firstName", "lastName"};
		} else {
			return new String[]{"id", "firstName", "lastName"};
		}
	}

	public Object[] getColumnValues() {
		if ("findId".equals(persistableMode)) {
			return new Object[]{id};
		} else if ("findName".equals(persistableMode)) {
			return new Object[]{firstName, lastName};
		} else {
			return new Object[]{id, firstName, lastName};
		}
	}

	public String getTableName() {
		return "author";
	}

	public boolean isValid() {
		return valid;
	}

	public boolean isNotValid() {
		return ! isValid();
	}

	static private PersistenceDelegate getPersistenceDelegate() {
		return ApplicationContext.getPersistenceDelegate();
	}

	public Long getId() {
		return id;
	}
	
	String getFirstName() {
		return firstName;
	}
	String getLastName() {
		return lastName;
	}

	public static List<Author> getAll() {
		List<Author> authors = new ArrayList<Author>();
		List<Persistable> findList = getPersistenceDelegate().findAll(new Author());
		if (findList.size() == 0) {
			// nothing to do
		} else {
			for (Persistable persistable : findList) {
				authors.add((Author)persistable);
			}
		}
		return authors;
	}
	
	public Author newFromDBColumns(List<Object> objectList) {
		Author author = new Author();
		if (objectList.size() == 3) {
			author.id = (Long) objectList.get(0);
			author.firstName = (String) objectList.get(1);
			author.lastName = (String) objectList.get(2);
		}
		return author;
	}

	@Override
	public void setId(Long id) {
		this.id = id;		
	}
}
