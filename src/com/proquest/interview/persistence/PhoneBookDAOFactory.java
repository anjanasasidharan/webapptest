package com.proquest.interview.persistence;

import com.proquest.interview.phonebook.business.Person;

/**
 * Factory for creating and returning appropriate DAO implementaion
 * @author Anjana Sasidharan
 *
 */
public class PhoneBookDAOFactory {

	@SuppressWarnings("rawtypes")
	public PhoneBookDAO getPhoneBookDAO(Class clazz) {
		if(clazz != null) {
			if(clazz.equals(Person.class)) {
				return new PersonDAOImpl();
			}
		}
		return null;
	}

}
