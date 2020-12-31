package ru.otus.validators;

import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.springframework.stereotype.Component;

import javax.validation.Validation;
import javax.validation.Validator;

@Component
public class BasicFieldValidator implements FieldValidator {

	private final Validator validator = Validation.byDefaultProvider()
			.configure()
			.messageInterpolator(new ParameterMessageInterpolator())
			.buildValidatorFactory()
			.getValidator();

	@Override
	public <T> boolean validate(final T obj) {
		return validator.validate(obj).size() == 0;
	}
}
