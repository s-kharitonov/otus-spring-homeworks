package ru.otus.exсeptions;

public class AuthorsServiceException extends RuntimeException {
	public AuthorsServiceException(final String message) {
		super(message);
	}
}
