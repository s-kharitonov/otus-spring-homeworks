package ru.otus.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.otus.domain.Answer;
import ru.otus.domain.Question;
import ru.otus.domain.QuestionDTO;
import ru.otus.domain.User;
import ru.otus.exceptions.TestServiceException;
import ru.otus.services.IOService;
import ru.otus.services.QuestionsService;
import ru.otus.services.TestService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class TestServiceImplNegativeUnitTest {

	private QuestionsService questionsService;
	private IOService ioService;
	private TestService testService;
	private User user;

	@BeforeEach
	void setUp() {
		this.questionsService = mock(QuestionsService.class);
		this.ioService = mock(IOService.class);
		this.testService = new TestServiceImpl(questionsService, ioService);
		this.user = new User("sergey", "kharitonov");
	}

	@Test
	@DisplayName("should throw TestServiceException when questions is empty")
	public void shouldThrowExceptionWhenQuestionsIsEmpty() {
		given(questionsService.getQuestions()).willReturn(new ArrayList<>());
		assertThrows(TestServiceException.class, () -> testService.test(user));
	}

	@Test
	@DisplayName("should throw TestServiceException when answers is empty")
	public void shouldThrowExceptionWhenAnswersIsEmpty() {
		final var question = new Question();

		question.setCorrectAnswer(1);
		question.setAnswers(new ArrayList<>());

		given(questionsService.getQuestions()).willReturn(List.of(new QuestionDTO(question)));
		assertThrows(TestServiceException.class, () -> testService.test(user));
	}

	@ParameterizedTest
	@ValueSource(strings = {"-3", "s"})
	@DisplayName("should throw IllegalArgumentException when not valid user answer")
	public void shouldThrowExceptionWhenNotValidUserAnswer(final String userAnswer) {
		final var question = new Question();

		question.setCorrectAnswer(1);
		question.setAnswers(List.of(new Answer(1, ""), new Answer(2, "")));

		given(questionsService.getQuestions()).willReturn(List.of(new QuestionDTO(question)));
		given(ioService.readLine()).willReturn(userAnswer);
		assertThrows(IllegalArgumentException.class, () -> testService.test(user));
	}

	@Test
	@DisplayName("should throw IllegalArgumentException when user is null")
	public void shouldThrowExceptionWhenUserIsNull() {
		assertThrows(IllegalArgumentException.class, () -> testService.test(null));
	}
}