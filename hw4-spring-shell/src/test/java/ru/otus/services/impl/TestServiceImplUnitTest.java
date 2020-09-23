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
import ru.otus.services.QuestionsService;
import ru.otus.services.TestService;
import ru.otus.services.facades.LocalizationIoFacade;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;

@SpringBootTest
class TestServiceImplUnitTest {

	private static final String USER_ANSWER = "1";

	@MockBean
	private QuestionsService questionsService;
	@MockBean
	private LocalizationIoFacade localizationIoFacade;
	@Autowired
	private TestService testService;
	private InOrder inOrder;
	private User user;
	private List<QuestionDTO> questions;

	@BeforeEach
	void setUp() {
		inOrder = inOrder(questionsService, localizationIoFacade);
		this.user = new User(USER_ANSWER, USER_ANSWER);
		this.questions = List.of(new QuestionDTO(buildQuestion()));
	}

	@Test
	@DisplayName("should throw IllegalArgumentException when user is null")
	public void shouldThrowExceptionWhenUserIsNull() {
		assertThrows(IllegalArgumentException.class, () -> testService.test(null));
	}

	@Test
	@DisplayName("should call questions service")
	public void shouldGettingQuestions() {
		given(questionsService.getQuestions()).willReturn(questions);
		given(localizationIoFacade.readLine()).willReturn(USER_ANSWER);
		testService.test(user);
		inOrder.verify(questionsService, times(1)).getQuestions();
	}

	@Test
	@DisplayName("should reading user answer")
	public void shouldReadingUserAnswer() {
		given(questionsService.getQuestions()).willReturn(questions);
		given(localizationIoFacade.readLine()).willReturn(USER_ANSWER);
		testService.test(user);
		inOrder.verify(localizationIoFacade, times(1)).readLine();
	}

	@Test
	@DisplayName("should print greeting")
	public void shouldPrintGreeting() {
		given(questionsService.getQuestions()).willReturn(questions);
		given(localizationIoFacade.readLine()).willReturn(USER_ANSWER);
		testService.test(user);
		inOrder.verify(localizationIoFacade, times(1))
				.writeMessageFromProps("test.name");
		inOrder.verify(localizationIoFacade, times(1))
				.writeMessageFromProps("user.good.luck", user.getName(), user.getSurname());
	}

	@Test
	@DisplayName("should throw TestServiceException when questions is empty")
	public void shouldThrowExceptionWhenQuestionsIsEmpty() {
		given(questionsService.getQuestions()).willReturn(new ArrayList<>());
		given(localizationIoFacade.readLine()).willReturn(USER_ANSWER);
		assertThrows(TestServiceException.class, () -> testService.test(user));
	}

	@Test
	@DisplayName("should throw TestServiceException when answers is empty")
	public void shouldThrowExceptionWhenAnswersIsEmpty() {
		final var question = new Question();

		question.setCorrectAnswer(1);
		question.setAnswers(new ArrayList<>());

		given(questionsService.getQuestions()).willReturn(List.of(new QuestionDTO(question)));
		given(localizationIoFacade.readLine()).willReturn(USER_ANSWER);
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
		given(localizationIoFacade.readLine()).willReturn(userAnswer);
		assertThrows(IllegalArgumentException.class, () -> testService.test(user));
	}

	private Question buildQuestion() {
		final var question = new Question();

		question.setCorrectAnswer(1);
		question.setAnswers(List.of(new Answer(1, ""), new Answer(2, "")));

		return question;
	}
}