package ru.otus.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import ru.otus.domain.Answer;
import ru.otus.domain.Question;
import ru.otus.domain.QuestionDTO;
import ru.otus.domain.User;
import ru.otus.services.IOService;
import ru.otus.services.QuestionsService;
import ru.otus.services.TestService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class TestServiceImplPositiveUnitTest {

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

	private Question buildQuestion() {
		final var question = new Question();

		question.setCorrectAnswer(1);
		question.setAnswers(List.of(new Answer(1, ""), new Answer(2, "")));

		return question;
	}
}