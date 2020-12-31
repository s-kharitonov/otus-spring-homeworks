package ru.otus.exсeptions;

public class BooksServiceException extends RuntimeException{
	public BooksServiceException(final String message) {
		super(message);
	}
}
