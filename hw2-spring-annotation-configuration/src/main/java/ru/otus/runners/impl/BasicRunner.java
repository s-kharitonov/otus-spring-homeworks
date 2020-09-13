package ru.otus.runners.impl;

import org.springframework.stereotype.Component;
import ru.otus.domain.User;
import ru.otus.runners.Runner;
import ru.otus.services.IOService;
import ru.otus.services.TestService;
import ru.otus.services.UserService;

@Component
public class BasicRunner implements Runner {

	private final IOService ioService;
	private final TestService testService;
	private final UserService userService;

	public BasicRunner(final TestService testService, final UserService userService, final IOService ioService) {
		this.testService = testService;
		this.userService = userService;
		this.ioService = ioService;
	}

	@Override
	public void run() {
		final User user = createUser();
		printTestResult(testService.test(user));
	}

	private void printTestResult(final int scores) {
		ioService.writeMessage(String.format("number of correct answers: %s", scores));
	}

	private User createUser() {
		ioService.writeMessage("enter your name:");
		final String name = ioService.readLine();

		ioService.writeMessage("enter your surname:");
		final String surname = ioService.readLine();

		return userService.createUser(name, surname);
	}
}
