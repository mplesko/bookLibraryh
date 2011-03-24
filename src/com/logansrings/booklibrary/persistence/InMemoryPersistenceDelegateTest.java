package com.logansrings.booklibrary.persistence;

import java.util.List;

import com.logansrings.booklibrary.domain.Author;

import junit.framework.TestCase;

public class InMemoryPersistenceDelegateTest extends TestCase {
	static PersistenceDelegate persistenceDelegate = 
		new InMemoryPersistenceDelegate();

	public void testPersist() {
		assertTrue(doPersist(1L, "name1a"));
		assertTrue(doPersist(11L, "name1b"));
		assertTrue(doPersist(2L, "name2a"));
		assertTrue(doPersist(22L, "name2b"));
		System.out.println(persistenceDelegate.toString());
		assertEquals(4, persistenceDelegate.findAll(new MyPersistable()).size());
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

		assertTrue(doExists(1L, "name1x"));
		assertFalse(doExists(9L, "name1a"));
		assertFalse(doExists(9L));
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
		return persistenceDelegate.exists(myPersistable);		
	}
	public boolean doExists(String name) {
		MyPersistable myPersistable = new MyPersistable();
		myPersistable.name = name;
		return persistenceDelegate.exists(myPersistable);		
	}

	public void testFindById() {
		MyPersistable myPersistable = new MyPersistable();
		myPersistable.id = 1L;
		Persistable persistable = persistenceDelegate.findOne(myPersistable);
		assertTrue(1L == persistable.getId());
		assertTrue(((MyPersistable)persistable).name.startsWith("name1"));
		
		myPersistable.setId(9L);
		assertNull(persistenceDelegate.findOne(myPersistable));
	}
	
	public void testFindOne() {
		MyPersistable myPersistable = new MyPersistable();
		myPersistable.id = 1L;
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
		persistable = persistenceDelegate.findOne(myPersistable);
		assertTrue(2L == persistable.getId());
		assertEquals("name2a", ((MyPersistable)persistable).name);

		myPersistable = new MyPersistable();
		myPersistable.id = 9L;
		persistable = persistenceDelegate.findOne(myPersistable);
		assertNull(persistable);
	}

	public void testFindAny() {
		MyPersistable myPersistable = new MyPersistable();
		myPersistable.id = 1L;
		List<Persistable> results = persistenceDelegate.findAny(myPersistable);
		assertEquals(1, results.size());
		assertTrue(1L == results.get(0).getId());
		assertTrue(((MyPersistable)results.get(0)).name.startsWith("name1"));

		assertTrue(1L == results.get(0).getId());
		assertTrue(((MyPersistable)results.get(0)).name.startsWith("name1"));

		myPersistable = new MyPersistable();
		myPersistable.id = 1L;
		myPersistable.name = "name1a";
		results = persistenceDelegate.findAny(myPersistable);
		assertEquals(1, results.size());
		assertTrue(1L == results.get(0).getId());
		assertEquals("name1a", ((MyPersistable)results.get(0)).name);

		myPersistable = new MyPersistable();
		myPersistable.name = "name2a";
		results = persistenceDelegate.findAny(myPersistable);
		assertEquals(1, results.size());
		assertTrue(2L == results.get(0).getId());
		assertEquals("name2a", ((MyPersistable)results.get(0)).name);

		myPersistable = new MyPersistable();
		myPersistable.id = 3L;
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

		private Long id;
		private String name;
		private boolean valid;
		private Integer version;

		@Override
		public Long getId() {return id;}
		@Override
		public void setId(Long id) {this.id = id;}
		@Override
		public boolean isValid() {return valid;}
		@Override
		public Integer getVersion() {return version;}
		@Override
		public void setVersion(Integer version) {this.version = version;}

		public String toString() {
			return String.format("[%s] id:%d name:%s valid:%s version:%d",
					"MyPersistable", id, name, valid, version);
		}
		public boolean equals(Object other) {
			if (other == null || !(other instanceof MyPersistable)) {
				return false;
			}		
			final MyPersistable that = (MyPersistable)other;
			return this == that || this.name.equals(that.name);
		}
		public int hashCode() {
			int hash = 7;
			hash = 31 * hash + (null == name ? 0 : name.hashCode());
			return hash;
		}
	}
}