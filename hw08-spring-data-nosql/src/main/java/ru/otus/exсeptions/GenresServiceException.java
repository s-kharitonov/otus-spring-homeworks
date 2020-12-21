package ru.otus.exсeptions;

public class GenresServiceException extends RuntimeException {
	public GenresServiceException(final String message) {
		super(message);
	}
}
