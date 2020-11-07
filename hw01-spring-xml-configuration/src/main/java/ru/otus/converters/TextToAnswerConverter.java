package ru.otus.converters;

import com.opencsv.bean.AbstractCsvConverter;
import ru.otus.domain.Answer;

public class TextToAnswerConverter extends AbstractCsvConverter {

	@Override
	public Object convertToRead(final String value) {
		return new Answer(value);
	}
}
