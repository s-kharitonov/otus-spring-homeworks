package ru.otus.converters;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.otus.domain.Constants;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class DateShellConverter implements Converter<String, Date> {

	private static final Logger logger = LoggerFactory.getLogger(DateShellConverter.class);
	private final DateFormat format = new SimpleDateFormat(Constants.DEFAULT_DATE_PATTERN);

	@Override
	public Date convert(final String source) {
		try {
			return format.parse(source);
		} catch (ParseException e) {
			logger.error("error parse date: {}", source, e);
			return null;
		}
	}
}
