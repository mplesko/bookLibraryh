package com.logansrings.booklibrary.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.logansrings.booklibrary.app.ApplicationProperties;
import com.logansrings.booklibrary.app.ApplicationUtilities;
import com.logansrings.booklibrary.domain.User;
import com.logansrings.booklibrary.notification.Notification;
import com.logansrings.booklibrary.notification.Severity;
import com.logansrings.booklibrary.notification.Type;
import com.logansrings.booklibrary.util.UniqueId;

/**
 * Wraps a DataBase
 */
public class DatabaseDelegate implements PersistenceDelegate {
	private static Connection connection = null;

	public static void main(String[] args) {
		User user = User.getTestUser();
		new DatabaseDelegate().findOne(user);
	}

	public boolean persist(Persistable persistable) {
		try {
			Connection conn = getConnection();
			try {
				persistable.setId(getId());
				conn.createStatement().executeUpdate(buildInsertSql(persistable));
				Notification.newNotification(
						this, "DataBaseDelegate.persist()", 
						persistable.getTableName() + " " + 
						persistable.toString() + " successful", "", 
						Type.DOMAIN, Severity.INFO);   
				return true;

			} finally {
				conn.close();
			}
		} catch (Exception e) {
			Notification.newNotification(
					this, "DataBaseDelegate.persist()", 
					persistable.getTableName() + " " + 
					persistable.toString() + " failed", e.getMessage(), 
					Type.TECHNICAL, Severity.ERROR, e);   
			return false;
		} 
	}

	private Long getId() {
		return UniqueId.getId();
	}

	public boolean exists(Persistable persistable) {
		boolean found = false;
		try {
			Connection conn = getConnection();
			try {
				ResultSet resultSet = conn.createStatement().executeQuery(
						buildSelectSql(persistable, true));
				int resultCount = 0;
				if (resultSet.last()) {
					resultCount = resultSet.getRow();
				}
				if (resultCount > 0) {
					found = true;
				}
			} finally {
				conn.close();
			}
		} catch (Exception e) {
			Notification.newNotification(
					this, "DataBaseDelegate.exists()", 
					persistable.toString() + " failed", e.getMessage(), 
					Type.TECHNICAL, Severity.ERROR);   
		}
		return found;
	}

	public Persistable findById(Persistable persistable) {
		return findOne(persistable);
	}
	
	public Persistable findOne(Persistable persistable) {
		try {
			Connection conn = getConnection();
			try {
				ResultSet resultSet = conn.createStatement().executeQuery(
						buildSelectSql(persistable, true));
				int resultCount = 0;
				if (resultSet.last()) {
					resultCount = resultSet.getRow();
				}
				if (resultCount == 0) {
					Notification.newNotification(
							this, "DataBaseDelegate.findOne()", 
							persistable.toString() + " failed", 
							"result size = 0", 
							Type.TECHNICAL, Severity.INFO);   
				} else if (resultCount > 1) {
					Notification.newNotification(
							this, "DataBaseDelegate.findOne()", 
							persistable.toString() + " failed", 
							"result size = " + resultCount, 
							Type.TECHNICAL, Severity.ERROR);   
				} else {
					resultSet.first();
					int columnCount = resultSet.getMetaData().getColumnCount();
					if (columnCount != persistable.getColumnCount()) {
						Notification.newNotification(
								this, "DataBaseDelegate.findOne()", 
								persistable.toString() + " failed", 
								"column count expected:" + 
								persistable.getColumnCount() +
								" actual:" + columnCount, 
								Type.TECHNICAL, Severity.ERROR);   
					} else {
						List<Object> list = new ArrayList<Object>();
						for (int i = 1; i <= columnCount; i++) {
							list.add(resultSet.getObject(i));
						}
						return persistable.newFromDBColumns(list);
					}
				}
			} finally {
				conn.close();
			}
		} catch (Exception e) {
			Notification.newNotification(
					this, "DataBaseDelegate.findOne()", 
					persistable.toString() + " failed", e.getMessage(), 
					Type.TECHNICAL, Severity.ERROR);   
		}
		return null;
	}

	public List<Persistable> findAny(Persistable persistable) {
		List<Persistable> returnList = new ArrayList<Persistable>();
		try {
			Connection conn = getConnection();
			try {
				ResultSet resultSet = conn.createStatement().executeQuery(
						buildSelectSql(persistable, true));
				int resultCount = 0;
				if (resultSet.last()) {
					resultCount = resultSet.getRow();
				}
				if (resultCount == 0) {
					// nothing found						
				} else {
					for (int i = 1; i <= resultCount; i++) {
						List<Object> objectList = new ArrayList<Object>();
						resultSet.absolute(i);
						for (int j = 1; j <= persistable.getColumnCount(); j++) {
							objectList.add(resultSet.getObject(j));
						}
						returnList.add(persistable.newFromDBColumns(objectList));
					}
				}
			} finally {
				conn.close();
			}
		} catch (Exception e) {
			Notification.newNotification(
					this, "DataBaseDelegate.findAny()", 
					persistable.toString() + " failed", e.getMessage(), 
					Type.TECHNICAL, Severity.ERROR);   
		}
		return returnList;
	}

	public List<Persistable> findAll(Persistable persistable) {
		List<Persistable> returnList = new ArrayList<Persistable>();
		try {
			Connection conn = getConnection();
			try {
				ResultSet resultSet = conn.createStatement().executeQuery(
						buildSelectSql(persistable, false));
				int resultCount = 0;
				if (resultSet.last()) {
					resultCount = resultSet.getRow();
				}
				if (resultCount == 0) {
					// nothing found						
				} else {
					for (int i = 1; i <= resultCount; i++) {
						List<Object> objectList = new ArrayList<Object>();
						resultSet.absolute(i);
						for (int j = 1; j <= persistable.getColumnCount(); j++) {
							objectList.add(resultSet.getObject(j));
						}
						returnList.add(persistable.newFromDBColumns(objectList));
					}
				}
			} finally {
				conn.close();
			}
		} catch (Exception e) {
			Notification.newNotification(
					this, "DataBaseDelegate.findAll()", 
					persistable.toString() + " failed", e.getMessage(), 
					Type.TECHNICAL, Severity.ERROR);   
		}
		return returnList;
	}

	public boolean delete(Persistable persistable) {
		try {
			Connection conn = getConnection();
			try {
				conn.createStatement().executeUpdate(buildDeleteSql(persistable));
				Notification.newNotification(
						this, "DataBaseDelegate.delete()", 
						persistable.getTableName() + " " + 
						persistable.toString() + " successful", "", 
						Type.DOMAIN, Severity.INFO);   
				return true;

			} finally {
				conn.close();
			}
		} catch (Exception e) {
			Notification.newNotification(
					this, "DataBaseDelegate.delete()", 
					persistable.toString() + " failed", e.getMessage(), 
					Type.TECHNICAL, Severity.ERROR);   
			return false;
		} 
	}

	private Connection getConnection() throws SQLException,
	InstantiationException, IllegalAccessException,
	ClassNotFoundException {
		if (connection == null) {
			// TODO move to properties
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			return DriverManager.getConnection(ApplicationProperties
					.getDatabaseURL(),
					ApplicationProperties.getDatabaseLogin(),
					ApplicationProperties.getDatabasePassword());
		} else {
			return connection;
		}
	}

	private String buildInsertSql(Persistable persistable) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder
		.append("INSERT INTO ")
		.append(persistable.getTableName())
		.append(" VALUES (")
		.append(ApplicationUtilities.arrayToFormattedCommaSeparatedString(
				persistable.getColumnValues()))
				.append(")");
		System.out.println(stringBuilder.toString());
		return stringBuilder.toString();
	}

	private String buildDeleteSql(Persistable persistable) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder
		.append("DELETE FROM ")
		.append(persistable.getTableName())
//		.append(" WHERE ")
		.append(buildWhereClause(persistable));

		System.out.println(stringBuilder.toString());
		return stringBuilder.toString();
	}
	
	private String buildSelectSql(
			Persistable persistable, boolean includeWhereClause) {
		
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder
		.append("SELECT * FROM ")
		.append(persistable.getTableName());
		if (includeWhereClause) {
			stringBuilder.append(buildWhereClause(persistable));
		}
		System.out.println(stringBuilder.toString());
		return stringBuilder.toString();
	}

	private String buildWhereClause(Persistable persistable) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(" WHERE ");
		int columnCount = persistable.getColumnNames().length;
		for (int i = 0; i < columnCount; i++) {
			if (i > 0) {
				stringBuilder.append(" AND ");
			}
			stringBuilder.append(persistable.getColumnNames()[i]);
			stringBuilder.append(" = ");
			if (persistable.getColumnValues()[i] instanceof String) {
				stringBuilder.append(
						"'" + persistable.getColumnValues()[i] + "'");
			} else {
				stringBuilder.append(persistable.getColumnValues()[i]);
			}
		}
		return stringBuilder.toString();
	}

}
