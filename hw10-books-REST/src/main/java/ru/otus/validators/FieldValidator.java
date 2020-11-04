package ru.otus.validators;

public interface FieldValidator {
	<T> boolean validate(T obj);
}
