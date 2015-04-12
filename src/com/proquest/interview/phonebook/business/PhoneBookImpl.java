package com.proquest.interview.phonebook.business;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.proquest.interview.exception.DataAccessException;
import com.proquest.interview.exception.PhoneBookAccessException;
import com.proquest.interview.persistence.PhoneBookDAO;
import com.proquest.interview.persistence.PhoneBookDAOFactory;
/**
 * Implementation of Phone book. 
 * All data is always in memory.
 * Add and Search entries in phone book. Also, print the entire phone book
 * @author Anjana Sasidharan
 *
 */
public class PhoneBookImpl implements PhoneBook {
	private Map<String, Person> people = null;
	private PhoneBookDAOFactory phoneBookDAOFactory = null;

	/**
	 * Initialize phone book and DAO factory
	 * @throws PhoneBookAccessException
	 */
	public PhoneBookImpl() throws PhoneBookAccessException {
		super();
		people = new HashMap<>();
		phoneBookDAOFactory = new PhoneBookDAOFactory();
		initializePhoneBook();
	}

	/**
	 * Load all data from database to phone book
	 * @throws PhoneBookAccessException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initializePhoneBook() throws PhoneBookAccessException{
		PhoneBookDAO dao = phoneBookDAOFactory.getPhoneBookDAO(Person.class);
		if(dao == null) {
			throw new PhoneBookAccessException("Phone book cannot be accessed!!");
		}
		try {
			people.putAll(dao.load());
		} catch (DataAccessException e) {
			throw new PhoneBookAccessException(e);
		}
	}	

	/**
	 * Add a new person. 
	 * If the person exists and have a different record, we will update the record
	 * If person exists and have same record, nothing happens
	 * If person doesn't exists, new entry is created in phone book
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addPerson(Person newPerson) throws PhoneBookAccessException {
		if(newPerson != null) {
			PhoneBookDAO dao = phoneBookDAOFactory.getPhoneBookDAO(Person.class);
			if(dao == null) {
				throw new PhoneBookAccessException("Phone book cannot be accessed!!");
			}
			String name = newPerson.getName();
			try {
				if(people.get(name) != null && !newPerson.equals(people.get(name))) {
					dao.update(newPerson);
				} else {
					dao.insert(newPerson);
				}
			} catch (DataAccessException e) {
				throw new PhoneBookAccessException(e);
			}
			people.put(newPerson.getName(), newPerson);
		}

	}

	/**
	 * Search for a person
	 * Map will provide a simple O(1) look up
	 */
	@Override
	public Person findPerson(String firstName, String lastName) {
		String name = new StringBuilder(firstName).append(SPACE)
				.append(lastName).toString();
		return people.get(name);
	}

	/**
	 * Utility function to print all people in the phone book
	 */
	@Override
	public void printPhoneBook() {
		Collection<Person> personList = people.values();
		for(Person person : personList) {
			System.out.println(person.toString());
		}	
	}	

	private static final String SPACE = " ";
}
