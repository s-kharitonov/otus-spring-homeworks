package ru.otus.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.domain.Answer;
import ru.otus.domain.Question;
import ru.otus.domain.QuestionDTO;
import ru.otus.domain.User;
import ru.otus.exceptions.TestServiceException;
import ru.otus.services.IOService;
import ru.otus.services.QuestionsService;
import ru.otus.services.TestService;
import ru.otus.services.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;

@SpringBootTest
class TestServiceImplUnitTest {

	private static final String TEST_NAME = ">>>>> ENGLISH LANGUAGE TEST <<<<<";
	private static final String USER_ANSWER = "1";

	@MockBean
	private QuestionsService questionsService;
	@MockBean
	private IOService ioService;
	@MockBean
	private UserService userService;
	@Autowired
	private TestService testService;
	private InOrder inOrder;
	private User user;
	private List<QuestionDTO> questions;

	@BeforeEach
	void setUp() {
		inOrder = inOrder(questionsService, ioService);
		this.user = new User(USER_ANSWER, USER_ANSWER);
		this.questions = List.of(new QuestionDTO(buildQuestion()));
	}

	@Test
	@DisplayName("should call questions service")
	public void shouldGettingQuestions() {
		given(questionsService.getQuestions()).willReturn(questions);
		given(ioService.readLine()).willReturn(USER_ANSWER);
		given(userService.createUser(USER_ANSWER, USER_ANSWER)).willReturn(user);
		testService.test();
		inOrder.verify(questionsService, times(1)).getQuestions();
	}

	@Test
	@DisplayName("should reading user answer")
	public void shouldReadingUserAnswer() {
		given(questionsService.getQuestions()).willReturn(questions);
		given(ioService.readLine()).willReturn(USER_ANSWER);
		given(userService.createUser(USER_ANSWER, USER_ANSWER)).willReturn(user);

		testService.test();

		inOrder.verify(ioService, times(1)).readLine();
	}

	@Test
	@DisplayName("should print greeting")
	public void shouldPrintGreeting() {
		given(questionsService.getQuestions()).willReturn(questions);
		given(ioService.readLine()).willReturn(USER_ANSWER);
		given(userService.createUser(USER_ANSWER, USER_ANSWER)).willReturn(user);

		testService.test();

		inOrder.verify(ioService, times(1)).writeMessage(TEST_NAME);
		inOrder.verify(ioService, times(1))
				.writeMessage(String.format("%s %s, good luck!", user.getName(), user.getSurname()));
	}

	@Test
	@DisplayName("should throw TestServiceException when questions is empty")
	public void shouldThrowExceptionWhenQuestionsIsEmpty() {
		given(questionsService.getQuestions()).willReturn(new ArrayList<>());
		given(userService.createUser(USER_ANSWER, USER_ANSWER)).willReturn(user);
		given(ioService.readLine()).willReturn(USER_ANSWER);
		assertThrows(TestServiceException.class, () -> testService.test());
	}

	@Test
	@DisplayName("should throw TestServiceException when answers is empty")
	public void shouldThrowExceptionWhenAnswersIsEmpty() {
		final var question = new Question();

		question.setCorrectAnswer(1);
		question.setAnswers(new ArrayList<>());

		given(questionsService.getQuestions()).willReturn(List.of(new QuestionDTO(question)));
		given(userService.createUser(USER_ANSWER, USER_ANSWER)).willReturn(user);
		given(ioService.readLine()).willReturn(USER_ANSWER);
		assertThrows(TestServiceException.class, () -> testService.test());
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
		given(userService.createUser(userAnswer, userAnswer)).willReturn(user);
		assertThrows(IllegalArgumentException.class, () -> testService.test());
	}

	private Question buildQuestion() {
		final var question = new Question();

		question.setCorrectAnswer(1);
		question.setAnswers(List.of(new Answer(1, ""), new Answer(2, "")));

		return question;
	}
}