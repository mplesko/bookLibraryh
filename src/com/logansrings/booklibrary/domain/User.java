package com.logansrings.booklibrary.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;
import javax.servlet.ServletException;

import javax.persistence.CascadeType;
import org.hibernate.annotations.GenericGenerator;

import com.logansrings.booklibrary.app.ApplicationContext;
import com.logansrings.booklibrary.app.ApplicationUtilities;
import com.logansrings.booklibrary.notification.Notification;
import com.logansrings.booklibrary.notification.Severity;
import com.logansrings.booklibrary.notification.Type;
import com.logansrings.booklibrary.persistence.Persistable;
import com.logansrings.booklibrary.persistence.PersistenceDelegate;
import com.logansrings.booklibrary.util.Encrypting;

@Entity
@Table( name = "user" )
public class User implements Persistable {
	@Id
	@Column(name = "id")
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private Long id;
	@Version
    @Column(name="version")
    private Integer version;
	
	@Column(name = "userName")
	private String userName = "";
	@Column(name = "password")
	private String password = "";

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "userbook",
			joinColumns = {@JoinColumn(name = "userId")},
			inverseJoinColumns = {@JoinColumn(name = "bookId")})
	private Set<Book> books = new HashSet<Book>();
	
	@Transient
	private boolean valid;
	@Transient
	private String context = "";
	@Transient
	private static Encrypting encrypting = null;

	public static void main(String[] args) throws ServletException {
		System.out.println(User.getTestUser().toString());
	}

	User() {}

	public static User find(String inUserName, String inPassword) {
		User user = new User(inUserName, inPassword, false);
		return user;
	}

	public static User create(String inUserName, String inPassword) {
		User user = new User(inUserName, inPassword, true);
		return user;
	}

	private User(String inUserName, String inPassword, boolean newUser) {
		if (ApplicationUtilities.isEmpty(inUserName, inPassword)) {
			valid = false;
			context = "must have userName and password";
			return;
		}
		this.userName = inUserName;
		if (newUser && getPersistenceDelegate().exists(this)) {
			valid = false;
			context = "already exists";
			return;
		}
		this.password = encryptPassword(inPassword);
		if (this.password == null) {
			valid = false;
			context = "unable to encrypt password";
			return;
		}
		if (newUser) {
			createAccount();
		} else {
			findAccount();
		}		
	}

	private void createAccount() {
		if (getPersistenceDelegate().persist(this)) {
			valid = true;
		} else {
			valid = false;
			context = "unable to persist user";
		}
	}

	private void findAccount() {
		Persistable persistable = getPersistenceDelegate().findOne(this);
		if (persistable == null) {
			valid = false;
			context = "invalid user name or password";
		} else {
			valid = true;
		}
	}

	/**
	 * @param password 
	 * @return encrypted password or null if process failed
	 */
	private String encryptPassword(String password) {
		String encryptedPassword = getEncrypting().encrypt(password);
		if (encryptedPassword == null) {
			Notification.newNotification(
					this, "User.encryptPassword()" , "failed", 
					"", Type.DOMAIN, Severity.ERROR);					
		}
		return encryptedPassword;
	}

	public boolean isValid() {return valid;}

	public boolean isNotValid() {return ! isValid();}

	public String getContext() {return context;}

	public String toString() {
		return String.format("[%s] id:%d userName:%s valid:%s context:%s version:%d",
				"User", id, userName, valid, context, version);
	}

	public static User getTestUser() {
		User user = new User();
		user.id = 1L;
		user.userName = "userName";
		user.password = "password";
		user.valid = true;
		user.context = "context";
		return user;
	}

	static void setEncrypting(Encrypting encrypting) {
		User.encrypting = encrypting;
	}
	static private Encrypting getEncrypting() {
		if (encrypting == null) {
			setEncrypting(Encrypting.getInstance());
		}
		return encrypting;
	}

	static private PersistenceDelegate getPersistenceDelegate() {
		return ApplicationContext.getPersistenceDelegate();
	}

	public Long getId() {return id;}
	@Override
	public void setId(Long id) {this.id = id;}
	
	@Override
	public Integer getVersion() {return version;}
	@Override
	public void setVersion(Integer version) {this.version = version;}

	public boolean equals(Object other) {
		if (other == null || !(other instanceof User)) {
			return false;
		}		
		final User that = (User)other;
		return this == that || (this.userName.equals(that.userName));
	}
	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + (null == userName ? 0 : userName.hashCode());
		return hash;
	}
}
