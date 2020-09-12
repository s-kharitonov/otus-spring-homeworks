package ru.otus.exceptions;

public class AnswersNotFoundException extends RuntimeException {

	public AnswersNotFoundException(final String message) {
		super(message);
	}
}
