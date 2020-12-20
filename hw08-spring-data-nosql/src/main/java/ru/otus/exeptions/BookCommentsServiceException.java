package ru.otus.exeptions;

public class BookCommentsServiceException extends RuntimeException{
	public BookCommentsServiceException(final String message) {
		super(message);
	}
}
