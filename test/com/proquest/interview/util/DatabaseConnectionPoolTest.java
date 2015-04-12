package com.proquest.interview.util;

import java.sql.Connection;

import junit.framework.TestCase;

import org.junit.Test;

import com.proquest.interview.exception.DataAccessException;

/**
 * Tests for Database Connection Pool  
 * @author Anjana Sasidharan
 *
 */
public class DatabaseConnectionPoolTest extends TestCase {

	@Test
	public void testUnInitializedPool() {
		try {
			DatabaseConnectionPool.getDatabaseConnectionPool().getConnection();
			fail();
		} catch (IllegalStateException e) {
			assertTrue(true);
		} catch (DataAccessException e) {
			fail();
		}
	}

	public void testGetConnection() {
		try {
			DatabaseConnectionPool.initConnectionPool(1);
			Connection connection = DatabaseConnectionPool.getDatabaseConnectionPool().getConnection();
			assertTrue(DatabaseConnectionPool.getDatabaseConnectionPool().getCurrentPoolSize() == 0);
			DatabaseConnectionPool.getDatabaseConnectionPool().returnConnection(connection);
			DatabaseConnectionPool.getDatabaseConnectionPool().shutdownPool();
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
	}

	public void testReleaseConnection() {
		try {
			DatabaseConnectionPool.initConnectionPool(1);
			Connection connection = DatabaseConnectionPool.getDatabaseConnectionPool().getConnection();
			DatabaseConnectionPool.getDatabaseConnectionPool().returnConnection(connection);
			assertTrue(DatabaseConnectionPool.getDatabaseConnectionPool().getCurrentPoolSize() == 1);
			DatabaseConnectionPool.getDatabaseConnectionPool().shutdownPool();
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
	}
}
