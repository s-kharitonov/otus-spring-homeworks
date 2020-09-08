package ru.otus.converters;

import com.opencsv.bean.AbstractCsvConverter;
import ru.otus.domain.Answer;

public class TextToAnswerConverter extends AbstractCsvConverter {

	private static final String SEPARATOR = "#";

	@Override
	public Object convertToRead(final String value) {
		final String[] parameters = value.split(SEPARATOR);

		if (parameters.length < 2) {
			throw new IllegalArgumentException("not found required parameters (number, text) in value!");
		}

		final int number = Integer.parseInt(parameters[0]);
		final String text = parameters[1];

		return new Answer(number, text);
	}
}
