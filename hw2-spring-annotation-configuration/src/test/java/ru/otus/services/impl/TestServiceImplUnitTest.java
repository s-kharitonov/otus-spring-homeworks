package ru.otus.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InOrder;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class TestServiceImplUnitTest {

	private static final String TEST_NAME = ">>>>> ENGLISH LANGUAGE TEST <<<<<";
	private static final String USER_ANSWER = "1";

	private QuestionsService questionsService;
	private IOService ioService;
	private TestService testService;
	private InOrder inOrder;
	private User user;
	private List<QuestionDTO> questions;

	@BeforeEach
	void setUp() {
		this.questionsService = mock(QuestionsService.class);
		this.ioService = mock(IOService.class);
		this.testService = new TestServiceImpl(questionsService, ioService);
		inOrder = inOrder(questionsService, ioService);
		this.user = new User("sergey", "kharitonov");
		this.questions = List.of(new QuestionDTO(buildQuestion()));
	}

	@Test
	@DisplayName("should call questions service")
	public void shouldGettingQuestions() {
		given(questionsService.getQuestions()).willReturn(questions);
		given(ioService.readLine()).willReturn(USER_ANSWER);
		testService.test(user);
		inOrder.verify(questionsService, times(1)).getQuestions();
	}

	@Test
	@DisplayName("should reading user answer")
	public void shouldReadingUserAnswer() {
		given(questionsService.getQuestions()).willReturn(questions);
		given(ioService.readLine()).willReturn(USER_ANSWER);
		testService.test(user);
		inOrder.verify(ioService, times(1)).readLine();
	}

	@Test
	@DisplayName("should print greeting")
	public void shouldPrintGreeting() {
		given(questionsService.getQuestions()).willReturn(questions);
		given(ioService.readLine()).willReturn(USER_ANSWER);
		testService.test(user);
		inOrder.verify(ioService, times(1)).writeMessage(TEST_NAME);
		inOrder.verify(ioService, times(1))
				.writeMessage(String.format("%s %s, good luck!", user.getName(), user.getSurname()));
	}

	@Test
	@DisplayName("should calculate test result scores")
	public void shouldCalculateTestResultScores() {
		given(questionsService.getQuestions()).willReturn(questions);
		given(ioService.readLine()).willReturn(USER_ANSWER);
		assertEquals(1, testService.test(user));
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

	private Question buildQuestion() {
		final var question = new Question();

		question.setCorrectAnswer(1);
		question.setAnswers(List.of(new Answer(1, ""), new Answer(2, "")));

		return question;
	}
}