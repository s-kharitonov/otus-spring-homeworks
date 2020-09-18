package ru.otus.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application")
public class AppProperties {
	private String questionsPath;

	public String getQuestionsPath() {
		return questionsPath;
	}

	public void setQuestionsPath(final String questionsPath) {
		this.questionsPath = questionsPath;
	}
}
