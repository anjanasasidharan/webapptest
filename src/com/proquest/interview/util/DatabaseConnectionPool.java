package com.proquest.interview.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.proquest.interview.exception.DataAccessException;

/**
 * Simple  implementation of a database connection pool.
 * I would have liked to use C3PO. 
 * @author Anjana Sasidharan
 *
 */
public class DatabaseConnectionPool {
	private int poolSize;
	private List<Connection> connections;
	private static DatabaseConnectionPool connectionPool;

	/**
	 * Singleton access to connection pool
	 * @return
	 */
	public static DatabaseConnectionPool getDatabaseConnectionPool() {
		if(connectionPool == null) {
			throw new IllegalStateException("Connection pool accessed without initializing");
		}
		return connectionPool;
	}

	/**
	 * private constructor to create a connection pool
	 * @param size
	 * @throws DataAccessException
	 */
	private DatabaseConnectionPool(int size) throws DataAccessException {
		poolSize = size;
		connections = new ArrayList<>(size);
		initConnections();
	}

	public static void initConnectionPool(int size) throws DataAccessException {
		connectionPool = new DatabaseConnectionPool(size);
	}

	/**
	 * Method to create database connections 
	 * @throws DataAccessException
	 */
	private void initConnections() throws DataAccessException {
		try {
			for(int i = 0; i < poolSize; i++) {
				connections.add(DatabaseUtil.getConnection());
			}
		} catch (ClassNotFoundException| SQLException e) {
			throw new DataAccessException(e);
		}
	}

	/**
	 * Application thread will get a connection from the pool. If a connection is not available create a new connection on the fly
	 * @return
	 * @throws DataAccessException
	 */
	public synchronized Connection getConnection() throws DataAccessException {
		Connection connection;
		try {
			if(connections.isEmpty()) {
				//So that requests won't fail due to unavailable connection
				connection = DatabaseUtil.getConnection();
			} else {
				connection = connections.remove(0);
			}
		} catch (ClassNotFoundException| SQLException e) {
			throw new DataAccessException(e);
		}	
		return connection;
	}

	/**
	 * Application thread can return the connection back. If the pool already has all the connections, then close the connection
	 * @param connection
	 * @throws DataAccessException
	 */
	public synchronized void returnConnection(Connection connection) throws DataAccessException {
		if(connection != null) {
			try {
				if(getCurrentPoolSize() < poolSize) {
					connections.add(connection);
				} else {
					connection.close();
				}
			} catch (SQLException e) {
				throw new DataAccessException(e);
			}
		}
	}

	/**
	 * Get number of connections available in the pool
	 * @return
	 */
	public synchronized  int getCurrentPoolSize() {
		if(connections != null) {
			return connections.size();
		}
		return 0;
	}

	/**
	 * Shutdown and release connection from the connection pool
	 * @throws DataAccessException
	 */
	public synchronized void shutdownPool() throws DataAccessException {
		try {
			if(connections != null) {
				for(Connection con : connections) {
					con.close();
				}
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}
}
