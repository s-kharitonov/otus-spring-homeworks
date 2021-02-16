package ru.otus.exceptions;

public class NotFoundStrategy extends RuntimeException{
	public NotFoundStrategy(final String message) {
		super(message);
	}
}
