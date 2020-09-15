package ru.otus.calculators.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.domain.Answer;
import ru.otus.domain.Question;
import ru.otus.domain.QuestionDTO;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BasicScoreCalculatorUnitTest {

	@Test
	@DisplayName("should calculate correct user answers")
	public void shouldCalculateCorrectUserAnswers() {
		final Map<QuestionDTO, Answer> userAnswers = new HashMap<>();
		final var question = new Question();
		final var answer = new Answer(1, "");

		question.setCorrectAnswer(1);
		userAnswers.put(new QuestionDTO(question), answer);

		assertEquals(1, new BasicScoreCalculator(userAnswers).calculate());
	}

	@Test
	@DisplayName("should throw NPE when user answers is null")
	public void shouldThrowExceptionWithNullParameter() {
		assertThrows(NullPointerException.class, () -> new BasicScoreCalculator(null).calculate());
	}

	@Test
	@DisplayName("should return result is equal to zero when user answers is empty")
	public void shouldReturnResultForEmptyParameter() {
		assertEquals(0, new BasicScoreCalculator(new HashMap<>()).calculate());
	}

	@Test
	@DisplayName("should ignore null keys")
	public void shouldIgnoreNullKeys() {
		final Map<QuestionDTO, Answer> userAnswers = new HashMap<>();

		userAnswers.put(null, new Answer(1, ""));

		assertEquals(0, new BasicScoreCalculator(userAnswers).calculate());
	}

	@Test
	@DisplayName("should ignore null values")
	public void shouldIgnoreNullValues() {
		final Map<QuestionDTO, Answer> userAnswers = new HashMap<>();

		userAnswers.put(new QuestionDTO(new Question()), null);

		assertEquals(0, new BasicScoreCalculator(userAnswers).calculate());
	}
}