package ru.otus.services.impl;

import org.springframework.stereotype.Service;
import ru.otus.calculators.impl.BasicScoreCalculator;
import ru.otus.domain.Answer;
import ru.otus.domain.QuestionDTO;
import ru.otus.domain.User;
import ru.otus.exceptions.TestServiceException;
import ru.otus.services.IOService;
import ru.otus.services.QuestionsService;
import ru.otus.services.TestService;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TestServiceImpl implements TestService {

	private final QuestionsService questionsService;
	private final IOService ioService;

	public TestServiceImpl(final QuestionsService questionsService, final IOService ioService) {
		this.questionsService = questionsService;
		this.ioService = ioService;
	}

	@Override
	public int test(User user) {

		if (Objects.isNull(user)) {
			throw new IllegalArgumentException("user is null!");
		}

		printGreeting(user);

		final List<QuestionDTO> questions = questionsService.getQuestions();
		final Map<QuestionDTO, Answer> userAnswers = new HashMap<>();

		if (questions.isEmpty()) {
			throw new TestServiceException("questions not found!");
		}

		for (QuestionDTO question : questions) {
			final Map<Integer, Answer> answerByNumber = buildAnswerByNumber(question.getAnswers());
			final List<Answer> answers = sortAnswersByNumber(question.getAnswers());

			if (answers.isEmpty()) {
				throw new TestServiceException(
						String.format("answers for question: %s, not found!", question.getText())
				);
			}

			ioService.writeMessage(question.getText());
			printAnswers(answers);
			ioService.writeMessage("Your answer:");

			final int answerNumber = parseAnswerNumberOrThrow(ioService.readLine());

			final int min = answers.get(0).getNumber();
			final int max = answers.get(answers.size() - 1).getNumber();

			if (answerNumber < min || answerNumber > max) {
				throw new IllegalArgumentException("non-existent answer number!");
			}

			userAnswers.put(question, answerByNumber.get(answerNumber));
		}

		return calculateScores(userAnswers);
	}

	private void printGreeting(final User user) {
		ioService.writeMessage(">>>>> ENGLISH LANGUAGE TEST <<<<<");
		ioService.writeMessage(String.format("%s %s, good luck!", user.getName(), user.getSurname()));
	}

	private int calculateScores(final Map<QuestionDTO, Answer> userAnswers) {
		return new BasicScoreCalculator(userAnswers).calculate();
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

	private List<Answer> sortAnswersByNumber(final List<Answer> answers) {
		return answers.stream()
				.sorted(Comparator.comparing(Answer::getNumber))
				.collect(Collectors.toList());
	}

	private Map<Integer, Answer> buildAnswerByNumber(final List<Answer> answers) {
		return answers.stream()
				.collect(Collectors.toMap(Answer::getNumber, answer -> answer));
	}
}
