/**
 * 
 */
package com.proquest.interview.persistence;

import junit.framework.TestCase;

import org.junit.Test;

import com.proquest.interview.phonebook.business.Person;

/**
 * Test for DAO factory
 * @author Anjana Sasidharan
 *
 */
public class PhoneBookDAOFactoryTest extends TestCase{

	@SuppressWarnings("rawtypes")
	@Test
	public void testGetDAOSuccess() {
		PhoneBookDAOFactory factory = new PhoneBookDAOFactory();
		PhoneBookDAO dao = factory.getPhoneBookDAO(Person.class);
		assertTrue(dao instanceof PersonDAOImpl);
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void testGetDAOFail() {
		PhoneBookDAOFactory factory = new PhoneBookDAOFactory();
		PhoneBookDAO dao = factory.getPhoneBookDAO(PhoneBookDAOFactoryTest.class);
		assertNull(dao);
	}

}
