package ru.otus.validators;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class FieldValidatorImpl implements FieldValidator{

	private final Validator validator;

	public FieldValidatorImpl(@Qualifier("webFluxValidator") final Validator validator) {
		this.validator = validator;
	}

	@Override
	public <T> Errors validate(final T target) {
		var errors = new BeanPropertyBindingResult(target, target.getClass().getName());

		validator.validate(target, errors);

		return errors;
	}
}
