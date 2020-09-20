package ru.otus.configs;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.nio.charset.StandardCharsets;

@Configuration
public class LocalizationConfig {

	@Bean
	public MessageSource messageSource() {
		var messageSource = new ReloadableResourceBundleMessageSource();

		messageSource.setBasename("classpath:/i18n/bundle");
		messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());

		return messageSource;
	}
}
