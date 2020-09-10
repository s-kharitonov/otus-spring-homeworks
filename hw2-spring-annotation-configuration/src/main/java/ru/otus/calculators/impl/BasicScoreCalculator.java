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
		final int correctAnswer = answerByQuestionEntry.getKey().getCorrectAnswer();
		final int answerNumber = answerByQuestionEntry.getValue().getNumber();

		return correctAnswer == answerNumber;
	}
}
