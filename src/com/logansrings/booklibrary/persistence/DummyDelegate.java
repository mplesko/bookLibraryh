package com.logansrings.booklibrary.persistence;

import java.util.List;

public class DummyDelegate implements PersistenceDelegate {
	public boolean persistReturnValue;
	public Persistable findOneReturnValue;
	public List<Persistable> findAnyReturnValue;
	public boolean existsReturnValue;

	public void setFindOneReturnValue(Persistable findOneReturnValue) {
		this.findOneReturnValue = findOneReturnValue;
	}

	public boolean persist(Persistable persistable) {
		return persistReturnValue;
	}

	public void setPersistReturnValue(boolean persistReturnValue) {
		this.persistReturnValue = persistReturnValue;
	}

	public Persistable findById(Persistable persistable) {
		return findOne(persistable);
	}
	
	public Persistable findOne(Persistable persistable) {
		return findOneReturnValue;
	}

	public boolean exists(Persistable persistable) {
		return existsReturnValue;
	}

	public List<Persistable> findAny(Persistable persistable) {
		return findAnyReturnValue;
	}

	@Override
	public boolean delete(Persistable persistable) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Persistable> findAll(Persistable persistable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean persist(PersistableComplex persistable,
			Persistable associatedPersistable) {
		// TODO Auto-generated method stub
		return false;
	}
}
