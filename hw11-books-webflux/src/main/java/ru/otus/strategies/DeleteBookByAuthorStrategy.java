package ru.otus.strategies;

public interface DeleteBookByAuthorStrategy {
	void delete(String id);

	String getFieldName();
}
