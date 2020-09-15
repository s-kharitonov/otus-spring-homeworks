package ru.otus.calculators.impl;

import ru.otus.calculators.ScoreCalculator;
import ru.otus.domain.Answer;
import ru.otus.domain.QuestionDTO;
import ru.otus.domain.UserAnswer;

import java.util.List;

public class BasicScoreCalculator implements ScoreCalculator {

	@Override
	public int calculate(final List<UserAnswer> userAnswers) {
		return (int) userAnswers.stream()
				.filter(this::isCorrectAnswer)
				.count();
	}

	private boolean isCorrectAnswer(final UserAnswer userAnswer) {
		final QuestionDTO question = userAnswer.getQuestion();
		final Answer answer = userAnswer.getAnswer();

		if (question == null || answer == null) {
			return false;
		}

		final int correctAnswer = question.getCorrectAnswer();
		final int answerNumber = answer.getNumber();

		return correctAnswer == answerNumber;
	}
}
