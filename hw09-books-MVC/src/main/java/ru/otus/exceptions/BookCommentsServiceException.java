package ru.otus.exceptions;

public class BookCommentsServiceException extends RuntimeException {
	public BookCommentsServiceException(final String message) {
		super(message);
	}
}
