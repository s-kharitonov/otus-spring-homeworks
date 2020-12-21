package ru.otus.exсeptions;

public class BookCommentsServiceException extends RuntimeException{
	public BookCommentsServiceException(final String message) {
		super(message);
	}
}
