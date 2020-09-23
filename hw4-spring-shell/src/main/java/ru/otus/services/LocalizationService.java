package ru.otus.services;

public interface LocalizationService {
	String localizeMessage(String key, Object... args);
}
