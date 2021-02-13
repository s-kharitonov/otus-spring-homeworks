package ru.otus.strategies;

public interface DeleteEntityStrategy {
	void delete(String id);

	String getCollectionFieldName();
}
