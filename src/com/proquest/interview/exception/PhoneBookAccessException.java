package com.proquest.interview.exception;

/**
 * Exception class for capture exceptions in accessing phone book
 * @author Anjana Sasidharan
 *
 */
public class PhoneBookAccessException extends Exception{

	private static final long serialVersionUID = 1L;

	public PhoneBookAccessException() {
		super();
	}

	public PhoneBookAccessException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PhoneBookAccessException(String message, Throwable cause) {
		super(message, cause);
	}

	public PhoneBookAccessException(String message) {
		super(message);
	}

	public PhoneBookAccessException(Throwable cause) {
		super(cause);
	}

}
