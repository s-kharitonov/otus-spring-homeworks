package ru.otus.exeptions;

public class BooksServiceException extends RuntimeException{
	public BooksServiceException(final String message) {
		super(message);
	}
}
