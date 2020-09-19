package ru.otus.services.impl;

import org.springframework.stereotype.Service;
import ru.otus.domain.Answer;
import ru.otus.domain.QuestionDTO;
import ru.otus.domain.User;
import ru.otus.domain.UserAnswer;
import ru.otus.exceptions.TestServiceException;
import ru.otus.services.*;
import ru.otus.services.facades.IoFacade;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TestServiceImpl implements TestService {

	private final QuestionsService questionsService;
	private final IoService ioService;
	private final UserService userService;
	private final ScoreCalculatorService scoreCalculatorService;
	private final IoFacade ioFacade;

	public TestServiceImpl(final QuestionsService questionsService,
						   final IoService ioService,
						   final UserService userService,
						   final ScoreCalculatorService scoreCalculatorService,
						   final IoFacade ioFacade) {
		this.questionsService = questionsService;
		this.ioService = ioService;
		this.userService = userService;
		this.scoreCalculatorService = scoreCalculatorService;
		this.ioFacade = ioFacade;
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

		ioService.writeMessage(question.getText());
		printAnswers(answers);
		ioFacade.writeMessage("user.answer");

		final int answerNumber = parseAnswerNumberOrThrow(ioService.readLine());

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
		ioFacade.writeMessage("test.result", scores);
	}

	private User createUser() {
		ioFacade.writeMessage("user.name");
		final String name = ioService.readLine();

		ioFacade.writeMessage("user.surname");
		final String surname = ioService.readLine();

		return userService.createUser(name, surname);
	}

	private void printGreeting(final User user) {
		ioFacade.writeMessage("test.name");
		ioFacade.writeMessage("user.good.luck", user.getName(), user.getSurname());
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
			ioService.writeMessage(answer.getNumber() + " : " + answer.getText());
		}
	}

	private Map<Integer, Answer> buildAnswerByNumber(final List<Answer> answers) {
		return answers.stream()
				.collect(Collectors.toMap(Answer::getNumber, answer -> answer));
	}
}
