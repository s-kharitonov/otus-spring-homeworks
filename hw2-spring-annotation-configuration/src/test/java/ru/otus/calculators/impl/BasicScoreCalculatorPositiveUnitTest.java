package ru.otus.calculators.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.domain.Answer;
import ru.otus.domain.Question;
import ru.otus.domain.QuestionDTO;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BasicScoreCalculatorPositiveUnitTest {

	@Test
	@DisplayName("should calculate correct user answers")
	public void shouldCalculateCorrectUserAnswers() {
		final Map<QuestionDTO, Answer> userAnswers = new HashMap<>();
		final var question = new Question();
		final var answer  = new Answer(1, "");

		question.setCorrectAnswer(1);
		userAnswers.put(new QuestionDTO(question), answer);

		assertEquals(1, new BasicScoreCalculator(userAnswers).calculate());
	}
}