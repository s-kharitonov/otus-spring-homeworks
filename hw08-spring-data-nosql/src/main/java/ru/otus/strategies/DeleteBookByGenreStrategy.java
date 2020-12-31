package ru.otus.strategies;

public interface DeleteBookByGenreStrategy {
	void delete(String id);

	String getFieldName();
}
