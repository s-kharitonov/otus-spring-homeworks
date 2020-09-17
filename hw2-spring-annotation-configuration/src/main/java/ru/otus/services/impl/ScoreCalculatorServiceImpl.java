package ru.otus.services.impl;

import org.springframework.stereotype.Service;
import ru.otus.domain.Answer;
import ru.otus.domain.QuestionDTO;
import ru.otus.domain.UserAnswer;
import ru.otus.services.ScoreCalculatorService;

import java.util.List;

@Service
public class ScoreCalculatorServiceImpl implements ScoreCalculatorService {

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
