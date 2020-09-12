package ru.otus.runners.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.otus.calculators.ScoreCalculator;
import ru.otus.calculators.impl.BasicScoreCalculator;
import ru.otus.domain.Answer;
import ru.otus.domain.QuestionDTO;
import ru.otus.domain.User;
import ru.otus.runners.Runner;
import ru.otus.services.IOService;
import ru.otus.services.TestService;
import ru.otus.services.UserService;

import java.util.Map;

@Component
public class BasicRunner implements Runner {

	private final static Logger logger = LoggerFactory.getLogger(BasicRunner.class);

	private final IOService ioService;
	private final TestService testService;
	private final UserService userService;

	public BasicRunner(final TestService testService,
					   final UserService userService,
					   final IOService ioService) {
		this.testService = testService;
		this.userService = userService;
		this.ioService = ioService;
	}

	@Override
	public void run() {
		ioService.writeMessage("enter your name:");

		final String name = ioService.readLine();

		ioService.writeMessage("enter your surname:");

		final String surname = ioService.readLine();
		final User user = userService.createUser(name, surname);

		ioService.writeMessage(">>>>> ENGLISH LANGUAGE TEST <<<<<");
		ioService.writeMessage(String.format("%s %s, good luck!", user.getName(), user.getSurname()));

		final Map<QuestionDTO, Answer> userAnswers = testService.test(user);
		final ScoreCalculator calculator = new BasicScoreCalculator(userAnswers);

		logger.info("number of correct answers: {}", calculator.calculate());
	}
}
