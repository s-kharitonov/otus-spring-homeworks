package ru.otus.validators;

import org.springframework.validation.Errors;

public interface FieldValidator {
	<T> Errors validate(T target);
}
