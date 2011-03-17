package com.logansrings.booklibrary.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

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
	@Version
    @Column(name="version")
    private Integer version;

	@Column(name = "firstName")
	private String firstName;
	@Column(name = "lastName")
	private String lastName;

	@Transient
	private boolean valid;
	@Transient
	private String context = "";
	@Transient
	private static final String UNINITIALIZED = "must have firstName and lastName";
	
	Author() {}
	
	private Author(Long authorId) {
		this.id = authorId;
		valid = true;
	}

	private Author(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
		if (ApplicationUtilities.isEmpty(firstName, lastName)) {
			valid = false;
			context = UNINITIALIZED;
		} else {
			valid = true;
		}
	}
	
	public static Author find(Long authorId) {
		Author author = new Author(authorId);
		Persistable persistable = getPersistenceDelegate().findById(author);
		if (persistable == null) {
			author.valid = false;
			return author; 
		}
		author = (Author)persistable;
		author.valid = true;
		return author;
	}

	public static Author find(String firstName, String lastName) {
		Author author = new Author(firstName, lastName);
		if (author.isNotValid()) {
			return author;
		}
		Persistable persistable = getPersistenceDelegate().findOne(author);
		if (persistable == null) {
			author.valid = false;
			return author;
		}
		author = (Author)persistable;
		author.valid = true;
		return author;
	}
	
	public static Author create(String firstName, String lastName) {
		Author author = new Author(firstName, lastName);
		if (author.isNotValid()) {
			return author;
		}
		if (getPersistenceDelegate().exists(author)) {
			author.valid = false;
			author.context = "already exists";
			return author;
		}
		if (getPersistenceDelegate().persist(author)) {
			author.valid = true;
		} else {
			author.valid = false;
			author.context = "unable to persist author";
		}
		return author;
	}
	
	public String toString() {
		return String.format("[%s] id:%d firstName:%s lastName:%s valid:%s context:%s version:%d",
				"Author", id, firstName, lastName, valid, context, version);
	}

	public static Author getTestAuthor() {
		Author author = new Author();
		author.id = 1L;
		author.firstName = "authorFirstName";
		author.lastName = "authorLastName";
		author.valid = true;
		author.context = "a context";
		author.version = 1;
		return author;
	}
	public static Author getTestAuthor(String firstName, String lastName) {
		Author author = new Author();
		author.firstName = firstName;
		author.lastName = lastName;
		author.valid = true;
		author.context = "a context";
		author.version = 1;
		return author;
	}

	public boolean isValid() {return valid;}
	public boolean isNotValid() {return ! isValid();}

	static private PersistenceDelegate getPersistenceDelegate() {
		return ApplicationContext.getPersistenceDelegate();
	}

	String getFirstName() {return firstName;}
	String getLastName() {return lastName;}

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
	
	@Override
	public void setId(Long id) {this.id = id;}
	public Long getId() {return id;}
	
    public Integer getVersion() {return version;}	
    public void setVersion(Integer version) {this.version = version;}	

	public boolean equals(Object other) {
		if (other == null || !(other instanceof Author)) {
			return false;
		}		
		final Author that = (Author)other;
		return this == that ||
				(this.firstName.equals(that.firstName) && 
						this.lastName.equals(that.lastName));
	}
	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + (null == firstName ? 0 : firstName.hashCode());
		hash = 31 * hash + (null == lastName ? 0 : lastName.hashCode());
		return hash;
	}
}