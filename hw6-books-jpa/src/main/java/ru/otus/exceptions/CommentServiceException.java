package ru.otus.exceptions;

public class CommentServiceException extends RuntimeException {
	public CommentServiceException(final String message) {
		super(message);
	}
}
