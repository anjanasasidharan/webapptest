package com.proquest.interview.phonebook;

import com.proquest.interview.exception.DataAccessException;
import com.proquest.interview.exception.PhoneBookAccessException;
import com.proquest.interview.phonebook.business.Person;
import com.proquest.interview.phonebook.business.PhoneBook;
import com.proquest.interview.phonebook.business.PhoneBookImpl;
import com.proquest.interview.util.DatabaseConnectionPool;
import com.proquest.interview.util.DatabaseUtil;

/**
 * This is an activity class which is the main class for running this phone book.
 * ASSUMPTIONS: 
 * 1) Name is unique in phone book and is primary key for indexing and searching
 * 2) Though I created a Database Connection pool, I am assuming single threaded application. 
 * So only one connection is created, but can be easily extended for multi-threaded application.
 * 3) Assuming entire phonebook is in memory. In the ideal world, I would have used a cache instead
 * @author Anjana Sasidharan
 *
 */
public class PhoneBookActivity {
	PhoneBook phoneBook;

	/**
	 * Initializing phone book application
	 * 1) Initialize in-memory Database
	 * 2) Initialize Database connection pool
	 * 3) Initialize phone book and load all data from database
	 * @throws DataAccessException
	 * @throws PhoneBookAccessException
	 */
	private void init() throws DataAccessException, PhoneBookAccessException {
		DatabaseUtil.initDB();
		DatabaseConnectionPool.initConnectionPool(1);
		phoneBook = new PhoneBookImpl();
	}

	/**
	 * Graceful shutdown of application
	 * 1) Clean up in-memory Database
	 * 2) Shutdown connection pool
	 * @throws DataAccessException
	 */
	private void shutDown() throws DataAccessException {
		DatabaseUtil.cleanDB();
		DatabaseConnectionPool.getDatabaseConnectionPool().shutdownPool();
	}

	/**
	 * Utility method to add people to phone book
	 * @param name
	 * @param phoneNumber
	 * @param address
	 * @throws PhoneBookAccessException
	 */
	private void createPeople(String name, String phoneNumber, String address) throws PhoneBookAccessException {
		Person newPerson = new Person(name, phoneNumber, address);
		phoneBook.addPerson(newPerson);
	}

	/**
	 * Utility method to print entire phone book
	 */
	private void printPhoneBook() {
		System.out.println("******************* Proquest Phone Book *******************");
		phoneBook.printPhoneBook();
		System.out.println("******************* End of Proquest Phone Book *******************");
	}

	/**
	 * Utility method to search for a name and print the details
	 * @param firstName
	 * @param lastName
	 * @throws PhoneBookAccessException
	 */
	private void findAndPrint(String firstName, String lastName) throws PhoneBookAccessException {
		System.out.println("******************* Details of "+firstName+" "+lastName+" *******************");
		Person person = phoneBook.findPerson(firstName, lastName);
		if(person != null) {
			System.out.println(person.toString());
		}
		System.out.println("*********************************************************");
	}


	public static void main(String[] args) {
		PhoneBookActivity activity = new PhoneBookActivity();
		//Initialize application
		try {
			activity.init();
		} catch (DataAccessException|PhoneBookAccessException e) {
			System.out.println("Failed to initialize phone book."+e.getMessage());
		} 

		/* create person objects and put them in the PhoneBook and database
		 * John Smith, (248) 123-4567, 1234 Sand Hill Dr, Royal Oak, MI
		 * Cynthia Smith, (824) 128-8758, 875 Main St, Ann Arbor, MI
		 */ 
		try {
			activity.createPeople("John Smith", "(248) 123-4567", "1234 Sand Hill Dr, Royal Oak, MI");
			activity.createPeople("Cynthia Smith", "(824) 128-8758", "875 Main St, Ann Arbor, MI");
		} catch (PhoneBookAccessException e) {
			System.out.println("Failed to create new people "+e.getMessage());
		}

		// print the phone book out to System.out
		activity.printPhoneBook();

		// find Cynthia Smith and print out just her entry
		try {
			activity.findAndPrint("Cynthia", "Smith");
		} catch (PhoneBookAccessException e) {
			System.out.println("Failed to search in phone book "+e.getMessage());
		}

		/* insert the new person objects into the database
		 * Note: While adding new people to phone book, they are already added to database 
		 */

		// Shutdown application
		try {
			activity.shutDown();
		} catch (DataAccessException e) {
			System.out.println("Failed to shutdown phone book."+e.getMessage());;
		}
	}

}
