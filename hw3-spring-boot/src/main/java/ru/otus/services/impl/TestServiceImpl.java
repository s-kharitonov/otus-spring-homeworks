package ru.otus.services.impl;

import org.springframework.stereotype.Service;
import ru.otus.domain.Answer;
import ru.otus.domain.QuestionDTO;
import ru.otus.domain.User;
import ru.otus.domain.UserAnswer;
import ru.otus.exceptions.TestServiceException;
import ru.otus.services.QuestionsService;
import ru.otus.services.ScoreCalculatorService;
import ru.otus.services.TestService;
import ru.otus.services.UserService;
import ru.otus.services.facades.LocalizationIoFacade;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TestServiceImpl implements TestService {

	private final QuestionsService questionsService;
	private final UserService userService;
	private final ScoreCalculatorService scoreCalculatorService;
	private final LocalizationIoFacade localizationIoFacade;

	public TestServiceImpl(final QuestionsService questionsService,
						   final UserService userService,
						   final ScoreCalculatorService scoreCalculatorService,
						   final LocalizationIoFacade localizationIoFacade) {
		this.questionsService = questionsService;
		this.userService = userService;
		this.scoreCalculatorService = scoreCalculatorService;
		this.localizationIoFacade = localizationIoFacade;
	}

	@Override
	public void test() {
		final User user = createUser();

		printGreeting(user);

		final List<QuestionDTO> questions = questionsService.getQuestions();
		final List<UserAnswer> userAnswers = new ArrayList<>();

		if (questions.isEmpty()) {
			throw new TestServiceException("questions not found!");
		}

		for (QuestionDTO question : questions) {
			final Answer answer = askQuestion(question);

			userAnswers.add(new UserAnswer(question, answer));
		}

		printTestResult(scoreCalculatorService.calculate(userAnswers));
	}

	private Answer askQuestion(final QuestionDTO question) {
		final Map<Integer, Answer> answerByNumber = buildAnswerByNumber(question.getAnswers());
		final List<Answer> answers = getAnswersOrThrow(question);

		localizationIoFacade.writeMessage(question.getText());
		printAnswers(answers);
		localizationIoFacade.writeMessageFromProps("user.answer");

		final int answerNumber = parseAnswerNumberOrThrow(localizationIoFacade.readLine());

		checkAnswer(answerNumber, answers);

		return answerByNumber.get(answerNumber);
	}

	private void checkAnswer(final int answerNumber, final List<Answer> answers) {
		final int min = answers.get(0).getNumber();
		final int max = answers.get(answers.size() - 1).getNumber();

		if (answerNumber < min || answerNumber > max) {
			throw new IllegalArgumentException("non-existent answer number!");
		}
	}

	private List<Answer> getAnswersOrThrow(final QuestionDTO question) {
		final List<Answer> answers = question.getAnswers();

		if (answers.isEmpty()) {
			throw new TestServiceException(
					String.format("answers for question: %s, not found!", question.getText())
			);
		} else {
			return answers;
		}
	}

	private void printTestResult(final int scores) {
		localizationIoFacade.writeMessageFromProps("test.result", scores);
	}

	private User createUser() {
		localizationIoFacade.writeMessageFromProps("user.name");
		final String name = localizationIoFacade.readLine();

		localizationIoFacade.writeMessageFromProps("user.surname");
		final String surname = localizationIoFacade.readLine();

		return userService.createUser(name, surname);
	}

	private void printGreeting(final User user) {
		localizationIoFacade.writeMessageFromProps("test.name");
		localizationIoFacade.writeMessageFromProps("user.good.luck", user.getName(), user.getSurname());
	}

	private int parseAnswerNumberOrThrow(final String answerNumber) {
		try {
			return Integer.parseInt(answerNumber);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("wrong answer format!");
		}
	}

	private void printAnswers(final List<Answer> answers) {
		for (Answer answer : answers) {
			localizationIoFacade.writeMessage(answer.getNumber() + " : " + answer.getText());
		}
	}

	private Map<Integer, Answer> buildAnswerByNumber(final List<Answer> answers) {
		return answers.stream()
				.collect(Collectors.toMap(Answer::getNumber, answer -> answer));
	}
}
