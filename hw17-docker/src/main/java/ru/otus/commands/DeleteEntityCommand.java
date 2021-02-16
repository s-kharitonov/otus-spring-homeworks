package ru.otus.commands;

import org.bson.Document;

public interface DeleteEntityCommand {
	void delete(String collectionName, Document source);
}
