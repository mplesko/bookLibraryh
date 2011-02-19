package com.logansrings.booklibrary.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.logansrings.booklibrary.app.ApplicationProperties;
import com.logansrings.booklibrary.app.ApplicationUtilities;
import com.logansrings.booklibrary.domain.User;
import com.logansrings.booklibrary.notification.Notification;
import com.logansrings.booklibrary.notification.Severity;
import com.logansrings.booklibrary.notification.Type;

/**
 * Wraps a Hibernate implmenation of persistence
 */
public class HibernateDelegate implements PersistenceDelegate {
	
	private static SessionFactory sessionFactory = getSessionFactory();
	private static Connection connection = null;

	public static void main(String[] args) {
		User user = User.getTestUser();
		new HibernateDelegate().findOne(user);
	}

	public boolean persist(Persistable persistable) {
		try {
			Session session = getSession();
			session.beginTransaction();
			session.save(persistable);
			session.getTransaction().commit();
			session.close();

			Notification.newNotification(
					this, "HibernateDelegate.persist()", 
					persistable.getTableName() + " " + 
					persistable.toString() + " successful", "", 
					Type.DOMAIN, Severity.INFO);   
			return true;

		} catch (HibernateException e) {
			Notification.newNotification(
					this, "HibernateDelegate.persist()", 
					persistable.getTableName() + " " + 
					persistable.toString() + " failed", e.getMessage(), 
					Type.TECHNICAL, Severity.ERROR, e);   
			return false;
		} 
	}

	public boolean exists(Persistable persistable) {
		boolean found = false;
		try {
			Connection conn = getConnection();
			try {
				ResultSet resultSet = conn.createStatement().executeQuery(
						buildSelectSql(persistable, true));
				int resultCount = 0;
				if (resultSet.last()) {
					resultCount = resultSet.getRow();
				}
				if (resultCount > 0) {
					found = true;
				}
			} finally {
				conn.close();
			}
		} catch (Exception e) {
			Notification.newNotification(
					this, "HibernateDelegate.exists()", 
					persistable.toString() + " failed", e.getMessage(), 
					Type.TECHNICAL, Severity.ERROR);   
		}
		return found;
	}

	public Persistable findById(Persistable persistable) {
		try {
			Session session = getSession();
			session.beginTransaction();
			return (Persistable)session.load(
					persistable.getClass(), persistable.getId());
//			Persistable found =
//				(Persistable)session.load(
//						persistable.getClass(), persistable.getId());
			
		} catch (HibernateException e) {
			e.printStackTrace();
			Notification.newNotification(
					this, "HibernateDelegate.findById()", 
					persistable.toString() + " failed", e.getMessage(), 
					Type.TECHNICAL, Severity.ERROR);   
			return null;
		}

	}
	
	public Persistable findOne(Persistable persistable) {
		try {
//			List<Persistable> list = new ArrayList<Persistable>();
			Session session = getSession();
			session.beginTransaction();
			List<Persistable> list = session.createSQLQuery(
					buildSelectSql(persistable, true)).
					addEntity(persistable.getClass()).list();
			if (list.isEmpty()) {
				Notification.newNotification(
						this, "HibernateDelegate.findOne()", 
						persistable.toString() + " failed", 
						"result size = 0", 
						Type.TECHNICAL, Severity.INFO);   
			} else if (list.size() > 1) {
				Notification.newNotification(
						this, "HibernateDelegate.findOne()", 
						persistable.toString() + " failed", 
						"result size = " + list.size(), 
						Type.TECHNICAL, Severity.ERROR);   
			}
			session.close();
			return list.get(0);
		} catch (HibernateException e) {
			Notification.newNotification(
					this, "HibernateDelegate.findOne()", 
					persistable.toString() + " failed", e.getMessage(), 
					Type.TECHNICAL, Severity.ERROR);   
			return null;
		}
	}

	public List<List<Object>> findAny(Persistable persistable) {
		List<List<Object>> returnList = new ArrayList<List<Object>>();
		try {
			Connection conn = getConnection();
			try {
				ResultSet resultSet = conn.createStatement().executeQuery(
						buildSelectSql(persistable, true));
				int resultCount = 0;
				if (resultSet.last()) {
					resultCount = resultSet.getRow();
				}
				if (resultCount == 0) {
					// nothing found						
				} else {
					for (int i = 1; i <= resultCount; i++) {
						List<Object> objectList = new ArrayList<Object>();
						resultSet.absolute(i);
						for (int j = 1; j <= persistable.getColumnCount(); j++) {
							objectList.add(resultSet.getObject(j));
						}
						returnList.add(objectList);
					}
				}
			} finally {
				conn.close();
			}
		} catch (Exception e) {
			Notification.newNotification(
					this, "HibernateDelegate.findAny()", 
					persistable.toString() + " failed", e.getMessage(), 
					Type.TECHNICAL, Severity.ERROR);   
		}
		return returnList;
	}

	public List<List<Object>> findAll(Persistable persistable) {
		List<List<Object>> returnList = new ArrayList<List<Object>>();
		try {
			Connection conn = getConnection();
			try {
				ResultSet resultSet = conn.createStatement().executeQuery(
						buildSelectSql(persistable, false));
				int resultCount = 0;
				if (resultSet.last()) {
					resultCount = resultSet.getRow();
				}
				if (resultCount == 0) {
					// nothing found						
				} else {
					for (int i = 1; i <= resultCount; i++) {
						List<Object> objectList = new ArrayList<Object>();
						resultSet.absolute(i);
						for (int j = 1; j <= persistable.getColumnCount(); j++) {
							objectList.add(resultSet.getObject(j));
						}
						returnList.add(objectList);
					}
				}
			} finally {
				conn.close();
			}
		} catch (Exception e) {
			Notification.newNotification(
					this, "HibernateDelegate.findAll()", 
					persistable.toString() + " failed", e.getMessage(), 
					Type.TECHNICAL, Severity.ERROR);   
		}
		return returnList;
	}

	public boolean delete(Persistable persistable) {
		try {
			Connection conn = getConnection();
			try {
				conn.createStatement().executeUpdate(buildDeleteSql(persistable));
				Notification.newNotification(
						this, "HibernateDelegate.delete()", 
						persistable.getTableName() + " " + 
						persistable.toString() + " successful", "", 
						Type.DOMAIN, Severity.INFO);   
				return true;

			} finally {
				conn.close();
			}
		} catch (Exception e) {
			Notification.newNotification(
					this, "HibernateDelegate.delete()", 
					persistable.toString() + " failed", e.getMessage(), 
					Type.TECHNICAL, Severity.ERROR);   
			return false;
		} 
	}

	private Connection getConnection() throws SQLException,
	InstantiationException, IllegalAccessException,
	ClassNotFoundException {
		if (connection == null) {
			// TODO move to properties
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			return DriverManager.getConnection(ApplicationProperties
					.getDatabaseURL(),
					ApplicationProperties.getDatabaseLogin(),
					ApplicationProperties.getDatabasePassword());
		} else {
			return connection;
		}
	}

	private String buildInsertSql(Persistable persistable) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder
		.append("INSERT INTO ")
		.append(persistable.getTableName())
		.append(" VALUES (")
		.append(ApplicationUtilities.arrayToFormattedCommaSeparatedString(
				persistable.getColumnValues()))
				.append(")");
		System.out.println(stringBuilder.toString());
		return stringBuilder.toString();
	}

	private String buildDeleteSql(Persistable persistable) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder
		.append("DELETE FROM ")
		.append(persistable.getTableName())
//		.append(" WHERE ")
		.append(buildWhereClause(persistable));

		System.out.println(stringBuilder.toString());
		return stringBuilder.toString();
	}
	
	private String buildSelectSql(
			Persistable persistable, boolean includeWhereClause) {
		
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder
		.append("SELECT * FROM ")
		.append(persistable.getTableName());
		if (includeWhereClause) {
			stringBuilder.append(buildWhereClause(persistable));
		}
		System.out.println(stringBuilder.toString());
		return stringBuilder.toString();
	}

	private String buildWhereClause(Persistable persistable) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(" WHERE ");
		int columnCount = persistable.getColumnNames().length;
		for (int i = 0; i < columnCount; i++) {
			if (i > 0) {
				stringBuilder.append(" AND ");
			}
			stringBuilder.append(persistable.getColumnNames()[i]);
			stringBuilder.append(" = ");
			if (persistable.getColumnValues()[i] instanceof String) {
				stringBuilder.append(
						"'" + persistable.getColumnValues()[i] + "'");
			} else {
				stringBuilder.append(persistable.getColumnValues()[i]);
			}
		}
		return stringBuilder.toString();
	}

	private static SessionFactory getSessionFactory() {
		SessionFactory sessionFactory = null; 
		try {
			sessionFactory = 
				new Configuration().configure().buildSessionFactory();
		} catch(HibernateException e) {
			Notification.newNotification(
					sessionFactory, "HibernateDelegate.getSessionFactory()", 
					"getSessionFactory() failed", e.getMessage(), 
					Type.TECHNICAL, Severity.ERROR);   
		}
		return sessionFactory;
	}
	private Session getSession() {
		return  sessionFactory.getCurrentSession();
	}
}
