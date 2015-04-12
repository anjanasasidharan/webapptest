package com.proquest.interview.phonebook.business;

import junit.framework.TestCase;

import org.junit.Test;

import com.proquest.interview.exception.PhoneBookAccessException;
import com.proquest.interview.util.DatabaseConnectionPool;
import com.proquest.interview.util.DatabaseUtil;

/**
 * Tests for PhoneBook Implementation
 * @author Anjana Sasidharan
 *
 */
public class PhoneBookImplTest extends TestCase{

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
	public void testAddNewPerson() {
		try {
			Person person = new Person("Andrew Alexis", "414-502-6363", "Seattle, WA");
			PhoneBook phoneBook = new PhoneBookImpl();
			phoneBook.addPerson(person);
			Person retrievedPerson = phoneBook.findPerson("Andrew", "Alexis");
			assertEquals(person, retrievedPerson);
		} catch (PhoneBookAccessException e) {
			fail();
		}

	}

	@Test
	public void testAddExistingPerson() {
		try {
			PhoneBook phoneBook = new PhoneBookImpl();
			Person person = phoneBook.findPerson("Chris", "Johnson");
			if(person != null) {
				phoneBook.addPerson(person);
				Person retrievedPerson = phoneBook.findPerson("Chris", "Johnson");
				assertEquals(person, retrievedPerson);
			} else {
				fail();
			}
		} catch (PhoneBookAccessException e) {
			fail();
		}
	}
	
	@Test
	public void testAddExistingPersonWithNewNumer() {
		try {
			PhoneBook phoneBook = new PhoneBookImpl();
			Person person = phoneBook.findPerson("Chris", "Johnson");
			if(person != null) {
				person.setPhoneNumber("414-502-6363");
				phoneBook.addPerson(person);
				Person retrievedPerson = phoneBook.findPerson("Chris", "Johnson");
				assertEquals(person, retrievedPerson);
			} else {
				fail();
			}
		} catch (PhoneBookAccessException e) {
			fail();
		}
	}
}
