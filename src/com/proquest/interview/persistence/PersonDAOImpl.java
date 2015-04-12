package com.proquest.interview.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.proquest.interview.exception.DataAccessException;
import com.proquest.interview.phonebook.business.Person;
import com.proquest.interview.util.DatabaseUtil;

/**
 * Implementation of Data access layer for Person object
 * Note: I would prefer using hibernate here for easy ORM.
 * but was not sure whether using a library is encouraged during interview
 * @author Anjana Sasidharan
 *
 */
public class PersonDAOImpl implements PhoneBookDAO<String, Person>{

	/**
	 * Insert a new person object to database
	 * Input Validation : Name cannot be null
	 * Output validation: If no row gets updated assuming it is a failure. 
	 * This is to avoid any silent failures 
	 */
	@Override
	public void insert(Person person) throws DataAccessException {
		validateInput(person);
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = DatabaseUtil.getPooledConnection();
			statement = connection.prepareStatement(INSERT);
			statement.setString(1, person.getName());
			statement.setString(2, person.getPhoneNumber());
			statement.setString(3, person.getAddress());
			int success = statement.executeUpdate();
			validateResult(success);
		} catch(SQLException e) {
			throw new DataAccessException(e);
		} finally {
			DatabaseUtil.releaseConnection(connection, statement, null);
		}

	}

	/**
	 * Update an existing row in the table
	 * Input Validation : Name cannot be null
	 * Output validation: If no row gets updated assuming it is a failure. 
	 * This is to avoid any silent failures 
	 * 
	 */
	@Override
	public void update(Person person) throws DataAccessException {
		validateInput(person);
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = DatabaseUtil.getPooledConnection();
			statement = connection.prepareStatement(UPDATE);
			statement.setString(1, person.getPhoneNumber());
			statement.setString(2, person.getAddress());
			statement.setString(3, person.getName());
			int success = statement.executeUpdate();
			validateResult(success);
		} catch(SQLException e) {
			throw new DataAccessException(e);
		} finally {
			DatabaseUtil.releaseConnection(connection, statement,null);
		}

	}

	/**
	 * Get Method used to retrieve data from database using primary key
	 */
	@Override
	public Person get(String name) throws DataAccessException {
		if(name!= null) {
			Connection connection = null;
			PreparedStatement statement = null;
			ResultSet resultSet = null;
			try {
				connection = DatabaseUtil.getPooledConnection();
				statement = connection.prepareStatement(SELECT);
				statement.setString(1, name);
				resultSet = statement.executeQuery();
				if(resultSet.next()) {
					return new Person(name, resultSet.getString(PHONENUMBER_COL), resultSet.getString(ADDRESS_COL));
				}
			} catch(SQLException e) {
				throw new DataAccessException(e);
			} finally {
				DatabaseUtil.releaseConnection(connection, statement, resultSet);
			}
		}
		return null;
	}

	/**
	 * Method used to load entire table
	 */
	@Override
	public Map<String, Person> load() throws DataAccessException {
		Map<String, Person> phoneBook = new HashMap<String, Person>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = DatabaseUtil.getPooledConnection();
			statement = connection.prepareStatement(SELECT_ALL);
			resultSet = statement.executeQuery();
			while(resultSet.next()) {
				phoneBook.put(resultSet.getString(NAME_COL), new Person(resultSet.getString(NAME_COL), 
						resultSet.getString(PHONENUMBER_COL), resultSet.getString(ADDRESS_COL)));
			}
		} catch(SQLException e) {
			throw new DataAccessException(e);
		} finally {
			DatabaseUtil.releaseConnection(connection, statement, resultSet);
		}
		return phoneBook;
	}

	/**
	 * Delete a row from database using primary key
	 */
	@Override
	public void delete(String name) throws DataAccessException {
		if(name != null) {
			Connection connection = null;
			PreparedStatement statement = null;
			try {
				connection = DatabaseUtil.getPooledConnection();
				statement = connection.prepareStatement(DELETE);
				statement.setString(1, name);
				statement.executeUpdate();
			} catch(SQLException e) {
				throw new DataAccessException(e);
			} finally {
				DatabaseUtil.releaseConnection(connection, statement,null);
			}
		}
	}

	/**
	 * Input validation. Name of the person cannot be null
	 */
	private void validateInput(Person person) throws DataAccessException {
		if(person == null || person.getName() == null) {
			throw new DataAccessException("Person or Name cannot be null");
		}
	}

	/**
	 * Insert/Update validation. Atleast one row should be inserted or updated
	 */
	private void validateResult(int success) throws DataAccessException {
		if(success <= 0) {
			throw new DataAccessException("No data got updated!!");
		}
	}

	private static final String INSERT = "INSERT INTO PHONEBOOK (NAME, PHONENUMBER, ADDRESS) VALUES(?,?,?)";
	private static final String UPDATE = "UPDATE PHONEBOOK SET PHONENUMBER = ?, ADDRESS = ? WHERE NAME = ?";
	private static final String SELECT = "SELECT PHONENUMBER, ADDRESS FROM PHONEBOOK WHERE NAME = ?";
	private static final String SELECT_ALL = "SELECT NAME, PHONENUMBER, ADDRESS FROM PHONEBOOK";
	private static final String DELETE = "DELETE FROM PHONEBOOK WHERE NAME = ?";
	private static final String NAME_COL = "NAME";
	private static final String PHONENUMBER_COL = "PHONENUMBER";
	private static final String ADDRESS_COL = "ADDRESS";


}
