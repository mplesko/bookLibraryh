package com.logansrings.booklibrary.persistence;

public interface Persistable {

	Long getId();
	void setId(Long id);
	Integer getVersion();
	void setVersion(Integer version);
//	String getTableName();
//	String[] getColumnNames();
//	Object[] getColumnValues();
//	int getColumnCount();
//	Persistable newFromDBColumns(List<Object> objectList);
	boolean isValid();

}
