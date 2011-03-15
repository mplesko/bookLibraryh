package com.logansrings.booklibrary.persistence;

import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Example;

import com.logansrings.booklibrary.app.ApplicationContext;
import com.logansrings.booklibrary.domain.Author;
import com.logansrings.booklibrary.domain.Book;
import com.logansrings.booklibrary.notification.Notification;
import com.logansrings.booklibrary.notification.Severity;
import com.logansrings.booklibrary.notification.Type;

/**
 * Wraps a Hibernate implementation of persistence
 */
public class HibernateDelegate implements PersistenceDelegate {
	private static SessionFactory sessionFactory = getSessionFactory();

	public static void main(String[] args) throws ServletException {
		new ApplicationContext().init();
		PersistenceDelegate hibernateDelegate = 
			ApplicationContext.getPersistenceDelegate();
		
		Author author = Author.getTestAuthor("a", "e");
		Persistable persistable;
		List<Persistable> persistables;
		int count;
		boolean bool;
		
		persistable = hibernateDelegate.findById(author);
		persistable = hibernateDelegate.findOne(author);
		count = hibernateDelegate.findAny(author).size();
		count = hibernateDelegate.findAll(author).size();
		bool = hibernateDelegate.exists(author);
		bool = hibernateDelegate.delete(author);
		
		bool = hibernateDelegate.persist(author);
		
		persistable = hibernateDelegate.findById(author);
		persistable = hibernateDelegate.findOne(author);
		count = hibernateDelegate.findAny(author).size();
		count = hibernateDelegate.findAll(author).size();
		bool = hibernateDelegate.exists(author);
		
		bool = hibernateDelegate.delete(author);
		
		count = hibernateDelegate.findAll(author).size();
		persistable = hibernateDelegate.findById(author);
		persistable = hibernateDelegate.findOne(author);
		
		Author fred = (Author)hibernateDelegate.findOne(
				Author.getTestAuthor("Fred", "Brooks"));
//		Author fred = Author.getTestAuthor("Fred", "Brooks");
		Book book1 = Book.getTestBook("The Mythical Man Month", fred);
//		bool = hibernateDelegate.persist(book1);
		
		Book book2 = Book.getTestBook("The Mythical Man Month", null);
			
		Book book3 = (Book) hibernateDelegate.findOne(book2);
		System.out.println(book3.toString());
		

	}

	public boolean persist(Persistable persistable) {
		Session session = getSession();
		try {
			session.beginTransaction();
			session.save(persistable);
			session.getTransaction().commit();
			Notification.newNotification(
					this, "HibernateDelegate.persist()", 
					persistable.toString() + " successful", "", 
					Type.DOMAIN, Severity.INFO);   
			return true;

		} catch (HibernateException e) {
			session.close();
			Notification.newNotification(
					this, "HibernateDelegate.persist()", 
					persistable.toString() + " failed", e.getMessage(), 
					Type.TECHNICAL, Severity.ERROR, e);   
			return false;
		}
	}

	public Persistable findById(Persistable persistable) {
//		if (persistable.getId() == null) {
//			Notification.newNotification(
//					this, "HibernateDelegate.findById()", 
//					persistable.toString() + " failed", "id is null", 
//					Type.TECHNICAL, Severity.ERROR);   
//			return null;
//		}
		
		Session session = getSession();
		try {
			session.beginTransaction();
			return (Persistable)session.load(
					persistable.getClass(), persistable.getId());
		} catch (Exception e) {
			Notification.newNotification(
					this, "HibernateDelegate.findById()", 
					persistable.toString() + " failed", e.getMessage(), 
					Type.TECHNICAL, Severity.ERROR);   
			return null;
		} finally {
			session.close();
		}
	}
	
	public boolean exists(Persistable persistable) {
		boolean found = false;
		try {
			List<Persistable> list = findQBE(persistable);
			if (! list.isEmpty()) {
				found = true;
			}
		} catch (HibernateException e) {
			Notification.newNotification(
					this, "HibernateDelegate.exists()", 
					persistable.toString() + " failed", e.getMessage(), 
					Type.TECHNICAL, Severity.ERROR);   
		}
		return found;
	}

	public Persistable findOne(Persistable persistable) {
		try {
			List<Persistable> list = findQBE(persistable);
			if (list.size() == 1) {
				return list.get(0);
			} else if (list.isEmpty()) {
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
						Type.TECHNICAL, Severity.INFO);
				return list.get(0);
			}			
		} catch (HibernateException e) {
			Notification.newNotification(
					this, "HibernateDelegate.findOne()", 
					persistable.toString() + " failed", e.getMessage(), 
					Type.TECHNICAL, Severity.ERROR);   
		}
		return null;
	}

	public List<Persistable> findAny(Persistable persistable) {
		try {
			List<Persistable> list = findQBE(persistable);
			return list;
		} catch (Exception e) {
			Notification.newNotification(
					this, "HibernateDelegate.findAny()", 
					persistable.toString() + " failed", e.getMessage(), 
					Type.TECHNICAL, Severity.ERROR);
			return Collections.emptyList();
		}
	}

	private List<Persistable> findQBE(Persistable persistable) throws HibernateException {
		Session session = getSession();
		try {
			session.beginTransaction();
			List list = session.createCriteria(persistable.getClass())
				.add( Example.create(persistable))
				.list();
			return list;
		} catch (HibernateException e) {
			throw e;
		} finally {
			session.close();
		}			
	}

	public List<Persistable> findAll(Persistable persistable) {
		Session session = getSession();
		try {
			session.beginTransaction();
			List list = session.createQuery(
					"from " + persistable.getClass().getName())
					.list();	
			return list;
		} catch (Exception e) {
			Notification.newNotification(
					this, "HibernateDelegate.findAll()", 
					persistable.toString() + " failed", e.getMessage(), 
					Type.TECHNICAL, Severity.ERROR);   
			return Collections.emptyList();
		} finally {
			session.close();
		}
	}

	public boolean delete(Persistable persistable) {
		Session session = getSession();
		try {
			session.beginTransaction();
			Persistable toDelete = (Persistable)session.get(
					persistable.getClass(), persistable.getId());
			if (toDelete == null) {
				throw new Exception("call to get() returned null");
			}
			session.delete(toDelete);
			session.getTransaction().commit();
			Notification.newNotification(
					this, "HibernateDelegate.delete()", 
					persistable.toString() + " successful", "", 
					Type.DOMAIN, Severity.INFO);   
			return true;
		} catch (Exception e) {
			Notification.newNotification(
					this, "HibernateDelegate.delete()", 
					persistable.toString() + " failed", e.getMessage(), 
					Type.TECHNICAL, Severity.ERROR);   
			session.close();
			return false;
		}
	}

	private static SessionFactory getSessionFactory() {
		SessionFactory sessionFactory = null; 
		try {
			sessionFactory = 
				new Configuration().configure().buildSessionFactory();
		} catch(Throwable throwable) {
			Notification.newNotification(
					sessionFactory, "HibernateDelegate.getSessionFactory()", 
					"getSessionFactory() failed", throwable.getMessage(), 
					Type.TECHNICAL, Severity.ERROR);   
		}
		return sessionFactory;
	}
	private Session getSession() {
		return  sessionFactory.getCurrentSession();
	}
}
