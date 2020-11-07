package ru.otus.exceptions;

public class BooksServiceException extends RuntimeException {
	public BooksServiceException(final String message) {
		super(message);
	}
}
