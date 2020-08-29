package ru.otus.domain;

import java.util.List;

public class Question {
	private final String text;
	private final List<Answer> answers;

	public Question(final String text, final List<Answer> answers) {
		this.text = text;
		this.answers = answers;
	}

	public String getText() {
		return text;
	}

	public List<Answer> getAnswers() {
		return answers;
	}

	@Override
	public String toString() {
		return "Question{" +
				"text='" + text + '\'' +
				", answers=" + answers +
				'}';
	}
}
