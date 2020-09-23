package ru.otus.services.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.domain.Answer;
import ru.otus.domain.Question;
import ru.otus.domain.QuestionDTO;
import ru.otus.domain.UserAnswer;
import ru.otus.services.ScoreCalculatorService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ScoreCalculatorServiceImplUnitTest {

	@Configuration
	public static class ScoreCalculatorServiceConfig {

		@Bean
		public ScoreCalculatorService scoreCalculatorService() {
			return new ScoreCalculatorServiceImpl();
		}
	}

	@Autowired
	private ScoreCalculatorService scoreCalculatorService;

	@Test
	@DisplayName("should calculate correct user answers")
	public void shouldCalculateCorrectUserAnswers() {
		final var question = new Question();
		final var answer = new Answer(1, "");

		question.setCorrectAnswer(1);

		final var userAnswers = List.of(new UserAnswer(new QuestionDTO(question), answer));

		assertEquals(1, scoreCalculatorService.calculate(userAnswers));
	}

	@Test
	@DisplayName("should return result is equal to zero when user answers is empty")
	public void shouldReturnResultForEmptyParameter() {
		assertEquals(0, scoreCalculatorService.calculate(new ArrayList<>()));
	}

	@Test
	@DisplayName("should ignore null values")
	public void shouldIgnoreNullValues() {
		final var userAnswers = List.of(new UserAnswer(null, null));
		assertEquals(0, scoreCalculatorService.calculate(userAnswers));
	}
}