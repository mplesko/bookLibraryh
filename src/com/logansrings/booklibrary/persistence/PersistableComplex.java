package com.logansrings.booklibrary.persistence;

public interface PersistableComplex extends Persistable {

	void setAssociatedPersistable(Persistable persistable);
}
