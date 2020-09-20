package ru.otus.services.impl;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.configs.AppProperties;
import ru.otus.services.LocalizationService;

@Service
public class LocalizationServiceImpl implements LocalizationService {

	private final AppProperties appProperties;
	private final MessageSource messageSource;

	public LocalizationServiceImpl(final AppProperties appProperties, final MessageSource messageSource) {
		this.appProperties = appProperties;
		this.messageSource = messageSource;
	}

	@Override
	public String localizeMessage(final String key, final Object... args) {
		return messageSource.getMessage(key, args, appProperties.getLocale());
	}
}
