package com.logansrings.booklibrary.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.logansrings.booklibrary.domain.Author;

/**
 * @author mark
 *
 */
public class InMemoryPersistenceDelegate implements PersistenceDelegate {
	private List<InMemoryObject> inMemoryObjects =
		new ArrayList<InMemoryObject>();

	public static void main(String [] args) {
		InMemoryPersistenceDelegate inMemoryPersistenceDelegate =
			new InMemoryPersistenceDelegate();

		Author author = Author.getTestAuthor();
		inMemoryPersistenceDelegate.persist(author);
		System.out.println(inMemoryPersistenceDelegate.toString());
		boolean exists = inMemoryPersistenceDelegate.exists(author);
	}

	public boolean persist(Persistable persistable) {
		InMemoryObject inMemoryObject =
			new InMemoryObject(persistable.getTableName());

		for (int i = 0; i < persistable.getColumnCount(); i++) {
			inMemoryObject.addColumn(
					persistable.getColumnNames()[i],
					persistable.getColumnValues()[i]);
		}
		inMemoryObjects.add(inMemoryObject);
		return true;
	}

	public Persistable findById(Persistable persistable) {
		return findOne(persistable);
	}
	
	public Persistable findOne(Persistable persistable) {
		for (InMemoryObject inMemoryObject :
			getInMemoryObjectsForTable(persistable.getTableName())) {

			boolean found = true;
			for (int i = 0; i < persistable.getColumnNames().length; i++) {
				if (inMemoryObject.hasColumn(
						persistable.getColumnNames()[i],
						persistable.getColumnValues()[i])) {
					continue;
				} else {
					found = false;
					break;
				}
			}
			if (found) {
				return persistable.newFromDBColumns(
						inMemoryObject.getValuesAsList());
			}                                                             
		}
		return null;                              
	}

	private List<InMemoryObject> getInMemoryObjectsForTable(String tableName) {
		List<InMemoryObject> objects = new ArrayList<InMemoryObject>();
		for (InMemoryObject inMemoryObject : inMemoryObjects) {
			if (inMemoryObject.isTable(tableName)) {
				objects.add(inMemoryObject);
			}
		}
		return objects;
	}

	@Override
	public boolean exists(Persistable persistable) {
		if (findOne(persistable) == null) {
			return false;
		} else {
			return true;
		}
	}

	public List<List<Object>> findAny(Persistable persistable) {
		List<List<Object>> returnList = new ArrayList<List<Object>>();
		for (InMemoryObject inMemoryObject :
			getInMemoryObjectsForTable(persistable.getTableName())) {

			boolean found = true;
			for (int i = 0; i < persistable.getColumnNames().length; i++) {
				if (inMemoryObject.hasColumn(
						persistable.getColumnNames()[i],
						persistable.getColumnValues()[i])) {
					continue;
				} else {
					found = false;
					break;
				}
			}
			if (found) {
				returnList.add(inMemoryObject.getValuesAsList());
			}                                                             
		}                             
		return returnList;
	}

	@Override
	public List<List<Object>> findAll(Persistable persistable) {
		List<List<Object>> returnList = new ArrayList<List<Object>>();
		for (InMemoryObject inMemoryObject :
			getInMemoryObjectsForTable(persistable.getTableName())) {
			returnList.add(inMemoryObject.getValuesAsList());
		}                             
		return returnList;
	}

	@Override
	public boolean delete(Persistable persistable) {
		for (InMemoryObject inMemoryObject :
			getInMemoryObjectsForTable(persistable.getTableName())) {

			boolean found = true;
			for (int i = 0; i < persistable.getColumnNames().length; i++) {
				if (inMemoryObject.hasColumn(
						persistable.getColumnNames()[i],
						persistable.getColumnValues()[i])) {
					continue;
				} else {
					found = false;
					break;
				}
			}
			if (found) {
				inMemoryObjects.remove(inMemoryObject);
				return true;
			}                                                             
		}
		return false;
	}

	public String toString() {
		StringBuilder result = new StringBuilder();
		for (InMemoryObject inMemoryObject : inMemoryObjects) {
			result.append(inMemoryObject.toString());
			result.append(" : ");
		}
		return result.toString();
	}


	/**
	 * @author mark
	 *
	 */
	 class InMemoryObject {
		private String tableName;
		private Map<String, Object> details = new HashMap<String, Object>();

		public InMemoryObject(String tableName) {
			this.tableName = tableName;
		}

		public boolean isTable(String tableName) {
			return this.tableName.equals(tableName);
		}

		public boolean hasColumn(String columnName, Object value) {
			//                                            System.out.println(value);
			//                                            System.out.println(details.get(columnName));
			//                                            Object o0 = details.get(columnName);
			//                                            Object o1 = details.containsKey(columnName);
			//                                            Object o2 = details.get(columnName).equals(value);
			if (details.containsKey(columnName)
					&& details.get(columnName).equals(value)) {
				return true;
			} else {
				return false;
			}
		}

		public void addColumn(String columnName, Object value) {
			details.put(columnName, value);                                            
		}                             

		public List<Object> getValuesAsList() {
			return new ArrayList<Object>(details.values());
		}

		public String toString() {
			StringBuilder result = new StringBuilder();
			result.append("[" + tableName + "] ");
			for (Entry<String, Object> entry : details.entrySet()) {
				result.append(String.format("column:%s value:%s | ",
						entry.getKey(), entry.getValue()));
			}
			return result.toString();
		}
	}

}
