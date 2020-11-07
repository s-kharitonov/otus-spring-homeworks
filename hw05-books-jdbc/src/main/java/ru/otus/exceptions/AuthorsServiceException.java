package ru.otus.exceptions;

public class AuthorsServiceException extends RuntimeException {
	public AuthorsServiceException(final String message) {
		super(message);
	}
}
