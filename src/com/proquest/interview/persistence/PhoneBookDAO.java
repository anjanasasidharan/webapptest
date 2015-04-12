package com.proquest.interview.persistence;

import java.util.Map;

import com.proquest.interview.exception.DataAccessException;

/**
 * Interface for all Data Access (CRUD) functions for phone book application
 * @author Anjana Sasidharan
 *
 * @param <K> key type
 * @param <V> business object that is mapped to the table
 */
public interface PhoneBookDAO<K,V> {

	public void insert(V value) throws DataAccessException;
	public void update(V value) throws DataAccessException;
	public void delete(K key) throws DataAccessException;
	public V get(K key) throws DataAccessException;
	public Map<K,V> load() throws DataAccessException;
	
}
