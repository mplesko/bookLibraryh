package com.logansrings.booklibrary.persistence;

import java.util.List;

public interface PersistenceDelegate {

	boolean exists(Persistable persistable);
	
	boolean persist(Persistable persistable);
	
	boolean delete(Persistable persistable);
	
	/**
	 * @param persistable
	 * @return a list of objects that are the column values of the peristable
	 * or an empty list if none found.
	 */
//	List<Object> findOne(Persistable persistable);

	/**
	 * @param persistable
	 * @return a persistable that's a match for the persistable argument 
	 * or null if not found.
	 */
	Persistable findById(Persistable persistable);
	
	/**
	 * @param persistable
	 * @return a persistable that's a match for the persistable argument 
	 * or null if not found.
	 */
	Persistable findOne(Persistable persistable);
	
	/**
	 * @param persistable
	 * @return a list of objects that are the column values of any peristable
	 * matching the persistable's values as filters, or an empty list if none found.
	 */
	List<List<Object>> findAny(Persistable persistable);
	
	/**
	 * @param persistable
	 * @return a list of objects that are the column values of any object 
	 * for the peristable's table.
	 */
	List<List<Object>> findAll(Persistable persistable);

}
