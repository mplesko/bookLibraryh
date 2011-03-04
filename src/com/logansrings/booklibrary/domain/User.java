package com.logansrings.booklibrary.domain;

import java.util.List;

import javax.servlet.ServletException;

import com.logansrings.booklibrary.app.ApplicationContext;
import com.logansrings.booklibrary.app.ApplicationUtilities;
import com.logansrings.booklibrary.notification.Notification;
import com.logansrings.booklibrary.notification.Severity;
import com.logansrings.booklibrary.notification.Type;
import com.logansrings.booklibrary.persistence.Persistable;
import com.logansrings.booklibrary.persistence.PersistenceDelegate;
import com.logansrings.booklibrary.util.Encrypting;
import com.logansrings.booklibrary.util.UniqueId;

public class User implements Persistable {

	private String persistableMode;
	private Long id;
	private String userName = "";
	private String password = "";
	private String encryptedPassword;
	private boolean valid;
	private String context = "";
	private static Encrypting encrypting = null;

	public static void main(String[] args) throws ServletException {
		System.out.println(User.getTestUser().toString());
	}

	private User() {}

	public static User find(String userName, String password) {
		User user = new User(userName, password, false);
		return user;
	}

	public static User create(String userName, String password) {
		User user = new User(userName, password, true);
		return user;
	}

//	public User(String userName, String password) {
//		this.userName = userName;
//		this.password = password;
//		if (ApplicationUtilities.isEmpty(userName, password)) {
//			valid = false;
//			context = "must have userName and password";
//			return;
//		}
//		persistableMode = "find";
//		if (getPersistenceDelegate().exists(this)) {
//			valid = false;
//			context = "already exists";
//		} else {
//			createAccount();
//		}
//	}

	private User(String userName, String password, boolean newUser) {
		this.userName = userName;
		this.password = password;
		if (ApplicationUtilities.isEmpty(userName, password)) {
			valid = false;
			context = "must have userName and password";
			return;
		}
		if (newUser) {
			persistableMode = "find";
			if (getPersistenceDelegate().exists(this)) {
				valid = false;
				context = "already exists";
			} else {
				createAccount();
			}
		} else {
			findAccount();
		}		
	}

	private void createAccount() {
		if (updateEncryptedPassword()) {
			persistableMode = "create";
			if (getPersistenceDelegate().persist(this)) {
				valid = true;
			} else {
				valid = false;
				context = "unable to persist user";
			}
		} else {
			valid = false;
			context = "unable to encrypt password";
		}
	}

	private void findAccount() {
		persistableMode = "find";
		Persistable persistable = getPersistenceDelegate().findOne(this);
		if (persistable == null) {
			valid = false;
			context = "user not found";
			return;
		}
		id = persistable.getId();
		encryptedPassword = ((User)persistable).encryptedPassword;

		String tempEncryptedPassword = getEncrypting().encrypt(password);
		if (encryptedPassword.equals(tempEncryptedPassword)) {
			valid = true;
		} else {
			valid = false;
			context = "invalid user name or password";
		}		
	}

	/**
	 * @return true if password is encrypted, false if not
	 */
	private boolean updateEncryptedPassword() {
		encryptedPassword = getEncrypting().encrypt(password);
		if (encryptedPassword == null) {
			Notification.newNotification(
					this, "User.updateEncryptedPassword()" , "failed", 
					"", Type.DOMAIN, Severity.ERROR);					
			return false;
		} else {
			return true;
		}
	}

	public boolean isValid() {
		return valid;
	}

	public boolean isNotValid() {
		return ! isValid();
	}

	public String getContext() {
		return context;
	}

	public Long getId() {
		return id;
	}

	public int getColumnCount() {
		return 3;
	}

	public String[] getColumnNames() {
		if ("find".equals(persistableMode)) {
			return new String[]{"userName"};
		} else {
			return new String[]{"id", "userName", "password"};
		}		
	}

	public Object[] getColumnValues() {
		if ("find".equals(persistableMode)) {
			return new Object[]{userName};
		} else {
			return new Object[]{id, userName, encryptedPassword};
		}		
	}

	public String getTableName() {
		return "user";
	}

	public String toString() {
		return String.format("[%s] id:%d userName:%s valid:%s context:%s",
				"User", id, userName, valid, context);
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
			return Encrypting.getInstance();
		} else {
			return encrypting;
		}
	}

	static private PersistenceDelegate getPersistenceDelegate() {
		return ApplicationContext.getPersistenceDelegate();
	}

	@Override
	public Persistable newFromDBColumns(List<Object> objectList) {
		User user = new User();
		if (objectList.size() == 3) {
			id = (Long)objectList.get(0);
			encryptedPassword = (String)objectList.get(2);
		}
		return user;
	}
	@Override
	public void setId(Long id) {
		this.id = id;		
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
