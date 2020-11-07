package ru.otus.domain;

public class Answer {
	private final int number;
	private final String text;

	public Answer(final int number, final String text) {
		this.number = number;
		this.text = text;
	}

	public int getNumber() {
		return number;
	}

	public String getText() {
		return text;
	}

	@Override
	public String toString() {
		return "Answer{" +
				"number=" + number +
				", text='" + text + '\'' +
				'}';
	}
}
