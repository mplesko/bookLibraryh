package com.logansrings.booklibrary.persistence;

import java.util.List;

public interface Persistable {

	Long getId();
	void setId(Long id);
	String getTableName();
	String[] getColumnNames();
	Object[] getColumnValues();
	int getColumnCount();
	Persistable newFromDBColumns(List<Object> objectList);

}
