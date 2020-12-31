package ru.otus.strategies;

public interface DeleteCommentByBookStrategy {
	void delete(String id);

	String getFieldName();
}
