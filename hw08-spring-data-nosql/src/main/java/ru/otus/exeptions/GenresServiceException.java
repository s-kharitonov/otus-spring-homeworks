package ru.otus.exeptions;

public class GenresServiceException extends RuntimeException {
	public GenresServiceException(final String message) {
		super(message);
	}
}
