package ru.otus.domain;

public class Answer {
	private final String text;

	public Answer(final String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	@Override
	public String toString() {
		return "Answer{" +
				"text='" + text + '\'' +
				'}';
	}
}
