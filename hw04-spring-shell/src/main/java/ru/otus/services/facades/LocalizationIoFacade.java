package ru.otus.services.facades;

public interface LocalizationIoFacade {
	void writeMessageFromProps(String key, Object... args);

	void writeMessage(String message);

	String readLine();
}
