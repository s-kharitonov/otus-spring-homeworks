package ru.otus.exсeptions;

public class NotFoundStrategy extends RuntimeException{
	public NotFoundStrategy(final String message) {
		super(message);
	}
}
