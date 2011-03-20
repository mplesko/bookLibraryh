package com.logansrings.booklibrary.persistence;

import java.util.List;

public interface PersistenceDelegate {

	boolean exists(Persistable persistable);
	
	boolean persist(Persistable persistable);
	boolean persist(PersistableComplex persistable, Persistable associatedPersistable);
	
	boolean delete(Persistable persistable);
	
	/**
	 * @param persistable
	 * @return the persistable that matches the persistable argument's id 
	 * or null if not found.
	 */
	Persistable findById(Persistable persistable);
	
	/**
	 * @param persistable
	 * @return the persistable that matches the persistable argument's values 
	 * or null if not found.
	 */
	Persistable findOne(Persistable persistable);
	
	/**
	 * @param persistable
	 * @return a list of persistables that match the persistable argument's values
	 * or an empty list if none found.
	 */
	List<Persistable> findAny(Persistable persistable);
	
	/**
	 * @param persistable
	 * @return all the object in the persistable argument's table
	 */
	List<Persistable> findAll(Persistable persistable);

}
