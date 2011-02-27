package com.logansrings.booklibrary.persistence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Example;

import com.logansrings.booklibrary.domain.Author;
import com.logansrings.booklibrary.notification.Notification;
import com.logansrings.booklibrary.notification.Severity;
import com.logansrings.booklibrary.notification.Type;

/**
 * Wraps a Hibernate implementation of persistence
 */
public class HibernateDelegate implements PersistenceDelegate {
	private static SessionFactory sessionFactory = getSessionFactory();

	public static void main(String[] args) {
		Author author = Author.getTestAuthor();
		HibernateDelegate hibernateDelegate = new HibernateDelegate();
		
		hibernateDelegate.findById(author);
		hibernateDelegate.findOne(author);
		hibernateDelegate.findAny(author);
		hibernateDelegate.findAll(author);
		hibernateDelegate.exists(author);
		hibernateDelegate.delete(author);
		
		hibernateDelegate.persist(author);
		
		hibernateDelegate.findById(author);
		hibernateDelegate.findOne(author);
		hibernateDelegate.findAny(author);
		hibernateDelegate.findAll(author);
		hibernateDelegate.exists(author);
		
		hibernateDelegate.delete(author);
		
		hibernateDelegate.findById(author);
		hibernateDelegate.findOne(author);

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

	public Persistable findById(Persistable persistable) {
		try {
			Session session = getSession();
//			session.beginTransaction();
			return (Persistable)session.load(
					persistable.getClass(), persistable.getId());
		} catch (HibernateException e) {
			Notification.newNotification(
					this, "HibernateDelegate.findById()", 
					persistable.toString() + " failed", e.getMessage(), 
					Type.TECHNICAL, Severity.ERROR);   
			return null;
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
			return list.get(0);
		} catch (HibernateException e) {
			Notification.newNotification(
					this, "HibernateDelegate.findOne()", 
					persistable.toString() + " failed", e.getMessage(), 
					Type.TECHNICAL, Severity.ERROR);   
			return null;
		}
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
			return Collections.EMPTY_LIST;
		}
	}

	private List<Persistable> findQBE(Persistable persistable) throws HibernateException {
			Session session = getSession();
			session.beginTransaction();
			List list = session.createCriteria(persistable.getClass())
				.add( Example.create(persistable))
				.list();
			session.close();
			return list;
	}

	public List<Persistable> findAll(Persistable persistable) {
		List<List<Object>> returnList = new ArrayList<List<Object>>();
		try {
			Session session = getSession();
			String q = "from " + persistable.getClass().getName();
			List list = session.createQuery(q).list();	
			session.close();
			return list;
		} catch (Exception e) {
			Notification.newNotification(
					this, "HibernateDelegate.findAll()", 
					persistable.toString() + " failed", e.getMessage(), 
					Type.TECHNICAL, Severity.ERROR);   
			return Collections.EMPTY_LIST;
		}
	}

	public boolean delete(Persistable persistable) {
		try {
			Session session = getSession();
			Persistable toDelete = (Persistable)session.load(
					persistable.getClass(), persistable.getId());
			session.delete(toDelete);
			Notification.newNotification(
					this, "HibernateDelegate.delete()", 
					persistable.getTableName() + " " + 
					persistable.toString() + " successful", "", 
					Type.DOMAIN, Severity.INFO);   
			return true;
		} catch (Exception e) {
			Notification.newNotification(
					this, "HibernateDelegate.delete()", 
					persistable.toString() + " failed", e.getMessage(), 
					Type.TECHNICAL, Severity.ERROR);   
			return false;
		} 
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
