package com.logansrings.booklibrary.persistence;

import java.util.ArrayList;
import java.util.List;

import com.logansrings.booklibrary.domain.Author;

/**
 * @author mark
 *
 */
public class InMemoryPersistenceDelegate implements PersistenceDelegate {
	private List<Persistable> inMemoryObjects =	new ArrayList<Persistable>();
	Long id = 1L;

	public static void main(String [] args) {
		InMemoryPersistenceDelegate inMemoryPersistenceDelegate =
			new InMemoryPersistenceDelegate();
		Author author = Author.getTestAuthor();
		Author author2 = Author.getTestAuthor();
		author2.setId(author.getId() + 1);
		inMemoryPersistenceDelegate.persist(author);
		inMemoryPersistenceDelegate.persist(author2);
		System.out.println(inMemoryPersistenceDelegate.toString());
		System.out.println(inMemoryPersistenceDelegate.exists(author));
		System.out.println(inMemoryPersistenceDelegate.exists(author2));
		Author author3 = Author.getTestAuthor();
		System.out.println(inMemoryPersistenceDelegate.exists(author3));
	}

	public boolean persist(Persistable persistable) {
		if (inMemoryObjects.contains(persistable)) {
			inMemoryObjects.remove(persistable);
		} else {
			persistable.setId(id++);
		}		
		inMemoryObjects.add(persistable);
		return true;
	}

	public Persistable findById(Persistable persistable) {
		if (persistable.getId() == null) {
			return null;
		} else {
			return findOne(persistable);
		}
//		for (Persistable inMemoryObject :
//			getInMemoryObjectsForClass(persistable.getClass())) {
//			if (inMemoryObject.getId() == persistable.getId()) {
//				return persistable;
//			}                                                             
//		}
//		return null;                              
	}
	
	public Persistable findOne(Persistable persistable) {
		List<Persistable> returnList = findAny(persistable);
		if (returnList.isEmpty()) {
			return null;
		} else {
			return returnList.get(0);
		}
//		if (persistable.getId() != null) {
//			return findById(persistable);
//		}
//		for (Persistable inMemoryObject :
//			getInMemoryObjectsForClass(persistable.getClass())) {
//			if (inMemoryObject.equals(persistable)) {
//				return persistable;
//			}                                                             
//		}
//		return null;                              
	}

	@Override
	public boolean exists(Persistable persistable) {
		return null != findOne(persistable);
	}

	public List<Persistable> findAny(Persistable persistable) {
		List<Persistable> returnList = new ArrayList<Persistable>();
		for (Persistable inMemoryObject : findAll(persistable)) {
			if (persistable.getId() != null && inMemoryObject.getId() == persistable.getId()
					|| persistable.getId() == null && inMemoryObject.equals(persistable)) {
				returnList.add(inMemoryObject);
			}                                                             
		}                             
		return returnList;
	}

	@Override
	public List<Persistable> findAll(Persistable persistable) {
		List<Persistable> objects = new ArrayList<Persistable>();
		for (Persistable inMemoryObject : inMemoryObjects) {
			if (inMemoryObject.getClass() == persistable.getClass()) {
				objects.add(inMemoryObject);
			}
		}
		return objects;
	}


	@Override
	public boolean delete(Persistable persistable) {
		for (Persistable inMemoryObject : findAll(persistable)) {
			if (inMemoryObject.getId() == persistable.getId()) {
				inMemoryObjects.remove(inMemoryObject);
				return true;
			}
		}
		return false;
	}

	public String toString() {
		StringBuilder result = new StringBuilder();
		for (Persistable inMemoryObject : inMemoryObjects) {
			result.append(inMemoryObject.toString());
			result.append(" : ");
		}
		return result.toString();
	}

	@Override
	public boolean persist(PersistableComplex persistable,
			Persistable associatedPersistable) {
		persistable.setAssociatedPersistable(
				findById(associatedPersistable));
		return persist(persistable);
	}
}
