package ru.otus.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Locale;

@ConfigurationProperties(prefix = "application")
public class AppProperties {
	private String questionsPath;
	private Locale locale;

	public String getQuestionsPath() {
		return questionsPath;
	}

	public void setQuestionsPath(final String questionsPath) {
		this.questionsPath = questionsPath;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(final Locale locale) {
		this.locale = locale;
	}
}
