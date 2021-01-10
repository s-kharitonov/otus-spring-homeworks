package ru.otus.exceptions;

public class BadRequestException extends RuntimeException {
	public BadRequestException(final String message) {
		super(message);
	}
}
