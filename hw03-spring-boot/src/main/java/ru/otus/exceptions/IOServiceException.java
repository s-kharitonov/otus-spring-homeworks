package ru.otus.exceptions;

public class IOServiceException extends RuntimeException {
	public IOServiceException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
