package ru.otus.calculators.impl;

import ru.otus.calculators.ScoreCalculator;
import ru.otus.domain.Answer;
import ru.otus.domain.QuestionDTO;

import java.util.Map;

public class BasicScoreCalculator implements ScoreCalculator {

	private final Map<QuestionDTO, Answer> userAnswers;

	public BasicScoreCalculator(final Map<QuestionDTO, Answer> userAnswers) {
		this.userAnswers = userAnswers;
	}

	@Override
	public int calculate() {
		return (int) userAnswers.entrySet()
				.stream()
				.filter(this::isCorrectAnswer)
				.count();
	}

	private boolean isCorrectAnswer(final Map.Entry<QuestionDTO, Answer> answerByQuestionEntry) {
		final QuestionDTO question = answerByQuestionEntry.getKey();
		final Answer answer = answerByQuestionEntry.getValue();

		if (question == null || answer == null) {
			return false;
		}

		final int correctAnswer = question.getCorrectAnswer();
		final int answerNumber = answer.getNumber();

		return correctAnswer == answerNumber;
	}
}
