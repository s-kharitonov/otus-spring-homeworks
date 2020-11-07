package ru.otus.exceptions;

public class GenresServiceException extends RuntimeException {
	public GenresServiceException(final String message) {
		super(message);
	}
}
