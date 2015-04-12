package com.proquest.interview.persistence;

import java.util.Map;

import junit.framework.TestCase;

import org.junit.Test;

import com.proquest.interview.exception.DataAccessException;
import com.proquest.interview.phonebook.business.Person;
import com.proquest.interview.util.DatabaseConnectionPool;
import com.proquest.interview.util.DatabaseUtil;

/**
 * Test for Person DAO
 * @author Anjana Sasidharan
 *
 */
public class PersonDAOImplTest extends TestCase{

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		DatabaseUtil.initDB();
		DatabaseConnectionPool.initConnectionPool(1);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		DatabaseUtil.cleanDB();
		DatabaseConnectionPool.getDatabaseConnectionPool().shutdownPool();
	}

	@Test
	public void testInsertSuccess() {
		try {
			Person person = new Person("Andrew Alexis", "414-502-6363", "Seattle, WA");
			PhoneBookDAO<String, Person> dao = new PersonDAOImpl();
			dao.insert(person);
			Person retrievedPerson = dao.get(person.getName());
			assertTrue(retrievedPerson != null && person.equals(retrievedPerson));
		} catch (DataAccessException e) {
			fail();
		}
	}

	@Test
	public void testInsertFail() {
		try {
			Person person = new Person(null, "414-502-6363", "Seattle, WA");
			PhoneBookDAO<String, Person> dao = new PersonDAOImpl();
			dao.insert(person);
			fail();
		} catch (DataAccessException e) {
			assertTrue(true);
		}
	}

	@Test
	public void testUpdateSuccess() {
		try {
			Person person = new Person("Andrew Alexis", "414-502-6363", "Seattle, WA");
			PhoneBookDAO<String, Person> dao = new PersonDAOImpl();
			dao.insert(person);
			person.setAddress(null);
			dao.update(person);
			Person retrievedPerson = dao.get(person.getName());
			assertTrue(retrievedPerson != null && person.equals(retrievedPerson));
		} catch (DataAccessException e) {
			fail();
		}
	}

	@Test
	public void testUpdateNullName() {
		try {
			Person person = new Person(null, "414-502-6363", "Seattle, WA");
			PhoneBookDAO<String, Person> dao = new PersonDAOImpl();
			dao.update(person);
			fail();
		} catch (DataAccessException e) {
			assertTrue(true);
		}
	}

	@Test
	public void testUpdateRecordNotPresent() {
		try {
			Person person = new Person("Helen Vario", "414-502-6363", "Seattle, WA");
			PhoneBookDAO<String, Person> dao = new PersonDAOImpl();
			dao.update(person);
			fail();
		} catch (DataAccessException e) {
			assertTrue(true);
		}
	}

	@Test
	public void testDeleteRecord() {
		try {
			PhoneBookDAO<String, Person> dao = new PersonDAOImpl();
			Person retrievedPerson = dao.get("Chris Johnson");
			assertNotNull(retrievedPerson);
			if(retrievedPerson != null) {
				dao.delete("Chris Johnson");
				retrievedPerson = dao.get("Chris Johnson");
				assertNull(retrievedPerson);
			}
		} catch (DataAccessException e) {
			fail();
		}
	}

	@Test
	public void testSelectRecord() {
		try {
			PhoneBookDAO<String, Person> dao = new PersonDAOImpl();
			Person retrievedPerson = dao.get("Chris Johnson");
			assertNotNull(retrievedPerson);
		} catch (DataAccessException e) {
			fail();
		}
	}

	@Test
	public void testSelectMissingRecord() {
		try {
			PhoneBookDAO<String, Person> dao = new PersonDAOImpl();
			Person retrievedPerson = dao.get("Chris Johnsons");
			assertNull(retrievedPerson);
		} catch (DataAccessException e) {
			fail();
		}
	}

	@Test
	public void testLoadAllRecord() {
		try {
			PhoneBookDAO<String, Person> dao = new PersonDAOImpl();
			Map<String, Person> phoneBook = dao.load();
			assertTrue(phoneBook != null && phoneBook.size() == 2);
		} catch (DataAccessException e) {
			fail();
		}
	}

}
