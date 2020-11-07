package ru.otus.domain;

import java.util.List;
import java.util.Objects;

public class QuestionDTO {
	private final int correctAnswer;
	private final List<Answer> answers;
	private final String text;

	public QuestionDTO(final Question question) {
		this.correctAnswer = question.getCorrectAnswer();
		this.answers = question.getAnswers();
		this.text = question.getText();
	}

	public int getCorrectAnswer() {
		return correctAnswer;
	}

	public List<Answer> getAnswers() {
		return answers;
	}

	public String getText() {
		return text;
	}

	@Override
	public String toString() {
		return "QuestionDTO{" +
				"correctAnswer=" + correctAnswer +
				", answers=" + answers +
				", text='" + text + '\'' +
				'}';
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (!(o instanceof QuestionDTO)) return false;
		final QuestionDTO that = (QuestionDTO) o;
		return getCorrectAnswer() == that.getCorrectAnswer() &&
				getAnswers().equals(that.getAnswers()) &&
				Objects.equals(getText(), that.getText());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getCorrectAnswer(), getAnswers(), getText());
	}
}
