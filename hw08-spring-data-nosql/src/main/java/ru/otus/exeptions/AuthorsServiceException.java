package ru.otus.exeptions;

public class AuthorsServiceException extends RuntimeException {
	public AuthorsServiceException(final String message) {
		super(message);
	}
}
