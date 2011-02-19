package com.logansrings.booklibrary.persistence;

import java.util.List;

import junit.framework.TestCase;


public class InMemoryPersistenceDelegateTest extends TestCase {

	//	MyPersistable myPersistable;
	static PersistenceDelegate persistenceDelegate = 
		new InMemoryPersistenceDelegate();

	public void testPersist() {
		assertTrue(doPersist(1L, "name1a"));
		assertTrue(doPersist(1L, "name1b"));
		assertTrue(doPersist(2L, "name2a"));
		assertTrue(doPersist(2L, "name2b"));
		System.out.println(persistenceDelegate.toString());
	}
	public boolean doPersist(Long id, String name) {
		MyPersistable myPersistable = new MyPersistable();
		myPersistable.id = id;
		myPersistable.name = name;
		return persistenceDelegate.persist(myPersistable);
	}

	public void testExists() {
		assertTrue(doExists(1L, "name1a"));
		assertTrue(doExists(1L));
		assertTrue(doExists("name1a"));	

		assertFalse(doExists(1L, "name1x"));
		assertFalse(doExists(3L, "name1a"));
		assertFalse(doExists(3L));
		assertFalse(doExists("name1x"));	
	}
	public boolean doExists(Long id, String name) {
		MyPersistable myPersistable = new MyPersistable();
		myPersistable.id = id;
		myPersistable.name = name;
		return persistenceDelegate.exists(myPersistable);		
	}
	public boolean doExists(Long id) {
		MyPersistable myPersistable = new MyPersistable();
		myPersistable.id = id;
		myPersistable.persistableMode = "findId";
		return persistenceDelegate.exists(myPersistable);		
	}
	public boolean doExists(String name) {
		MyPersistable myPersistable = new MyPersistable();
		myPersistable.name = name;
		myPersistable.persistableMode = "findName";
		return persistenceDelegate.exists(myPersistable);		
	}

	public void testFindOne() {
		MyPersistable myPersistable = new MyPersistable();
		myPersistable.id = 1L;
		myPersistable.persistableMode = "findId";
		Persistable persistable = persistenceDelegate.findOne(myPersistable);
		assertTrue(1L == persistable.getId());
		assertTrue(((MyPersistable)persistable).name.startsWith("name1"));

		myPersistable = new MyPersistable();
		myPersistable.id = 1L;
		myPersistable.name = "name1a";
		persistable = persistenceDelegate.findOne(myPersistable);
		assertTrue(1L == persistable.getId());
		assertEquals("name1a", ((MyPersistable)persistable).name);

		myPersistable = new MyPersistable();
		myPersistable.name = "name2a";
		myPersistable.persistableMode = "findName";
		persistable = persistenceDelegate.findOne(myPersistable);
		assertTrue(2L == persistable.getId());
		assertEquals("name2a", ((MyPersistable)persistable).name);

		myPersistable = new MyPersistable();
		myPersistable.id = 3L;
		myPersistable.persistableMode = "findId";
		persistable = persistenceDelegate.findOne(myPersistable);
		assertNull(persistable);
	}

	public void testFindAny() {
		MyPersistable myPersistable = new MyPersistable();
		myPersistable.id = 1L;
		myPersistable.persistableMode = "findId";
		List<List<Object>> results = persistenceDelegate.findAny(myPersistable);
		assertEquals(2, results.size());
		assertEquals(2, results.get(0).size());
		assertTrue(1L == (Long)results.get(0).get(0));
		assertTrue(((String)results.get(0).get(1)).startsWith("name1"));

		assertEquals(2, results.get(1).size());
		assertTrue(1L == (Long)results.get(0).get(0));
		assertTrue(((String)results.get(1).get(1)).startsWith("name1"));

		myPersistable = new MyPersistable();
		myPersistable.id = 1L;
		myPersistable.name = "name1a";
		myPersistable.persistableMode = "findName";
		results = persistenceDelegate.findAny(myPersistable);
		assertEquals(1, results.size());
		assertEquals(2, results.get(0).size());
		assertTrue(1L == (Long)results.get(0).get(0));
		assertEquals("name1a", (String)results.get(0).get(1));

		myPersistable = new MyPersistable();
		myPersistable.name = "name2a";
		myPersistable.persistableMode = "findName";
		results = persistenceDelegate.findAny(myPersistable);
		assertEquals(1, results.size());
		assertEquals(2, results.get(0).size());
		assertTrue(2L == (Long)results.get(0).get(0));
		assertEquals("name2a", (String)results.get(0).get(1));

		myPersistable = new MyPersistable();
		myPersistable.id = 3L;
		myPersistable.persistableMode = "findId";
		results = persistenceDelegate.findAny(myPersistable);
		assertEquals(0, results.size());
	}

	public void testDelete() {
		assertFalse(doExists(3L, "name1a"));

		MyPersistable myPersistable = new MyPersistable();
		myPersistable.id = 3L;
		myPersistable.name = "name1a";
		assertFalse(persistenceDelegate.delete(myPersistable));

		assertTrue(doExists(1L, "name1a"));
		
		myPersistable = new MyPersistable();
		myPersistable.id = 1L;
		myPersistable.name = "name1a";
		assertTrue(persistenceDelegate.delete(myPersistable));

		assertFalse(doExists(1L, "name1a"));
	}
	
	class MyPersistable implements Persistable {

		private String persistableMode;
		private Long id;
		private String name;
		private boolean valid;

		public void create(Long id, String name) {
			this.id = id;
			this.name = name;
			persistableMode = "create";
			if (persistenceDelegate.persist(this)) {
				// ok, expected
			} else {
				valid = false;
			}
		}

		private void findByName() {
			persistableMode = "findName";
			Persistable persistable = persistenceDelegate.findOne(this);
			if (persistable == null) {
				valid = false;
			} else {
				valid = true;
				id = persistable.getId();
			}
		}

		private void findById() {
			persistableMode = "findId";
			Persistable persistable = persistenceDelegate.findOne(this);
			if (persistable == null) {
				valid = false;
			} else {
				valid = true;
				name = ((MyPersistable)persistable).name;
			}
		}

		@Override
		public int getColumnCount() {
			return 2;
		}

		@Override
		public String[] getColumnNames() {
			if ("findId".equals(persistableMode)) {
				return new String[]{"id"};
			} else if ("findName".equals(persistableMode)) {
				return new String[]{"name"};
			} else {
				return new String[]{"id", "name"};
			}
		}

		@Override
		public Object[] getColumnValues() {
			if ("findId".equals(persistableMode)) {
				return new Object[]{id};
			} else if ("findName".equals(persistableMode)) {
				return new Object[]{name};
			} else {
				return new Object[]{id, name};
			}
		}

		@Override
		public String getTableName() {
			return "test";
		}

		@Override
		public Long getId() {
			return id;
		}

		@Override
		public Persistable newFromDBColumns(List<Object> objectList) {
			MyPersistable myPersistable = new MyPersistable();
			if (objectList.size() == 2) {
				myPersistable.id = (Long) objectList.get(0);
				myPersistable.name = (String)objectList.get(1);
			}
			return myPersistable;
		}

		@Override
		public void setId(Long id) {
			this.id = id;			
		}

	}

}