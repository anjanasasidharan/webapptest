package com.proquest.interview.phonebook.business;

import com.proquest.interview.exception.PhoneBookAccessException;
/**
 * Interface class for all the actions done in this phone book application
 * @author Anjana Sasidharan
 *
 */
public interface PhoneBook {
	/**
	 * Method to search for a person in phone book
	 * @param firstName
	 * @param lastName
	 * @return
	 * @throws PhoneBookAccessException
	 */
	public Person findPerson(String firstName, String lastName) throws PhoneBookAccessException;

	/**
	 * Method to add a person to phone book
	 * @param newPerson
	 * @throws PhoneBookAccessException
	 */
	public void addPerson(Person newPerson) throws PhoneBookAccessException;

	/**
	 * Method to print the phone book
	 */
	public void printPhoneBook();
}
