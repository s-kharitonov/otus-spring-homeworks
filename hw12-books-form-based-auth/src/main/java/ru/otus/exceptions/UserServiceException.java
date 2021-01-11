package ru.otus.exceptions;

public class UserServiceException extends RuntimeException{
	public UserServiceException(final String message) {
		super(message);
	}
}
