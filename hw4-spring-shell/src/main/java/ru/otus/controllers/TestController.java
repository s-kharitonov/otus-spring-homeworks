package ru.otus.controllers;

import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import ru.otus.domain.User;
import ru.otus.services.LocalizationService;
import ru.otus.services.TestService;
import ru.otus.services.UserService;

@ShellComponent
public class TestController {

	private final UserService userService;
	private final TestService testService;
	private final LocalizationService localizationService;
	private User user;

	public TestController(final UserService userService,
						  final TestService testService,
						  final LocalizationService localizationService) {
		this.userService = userService;
		this.testService = testService;
		this.localizationService = localizationService;
	}

	@ShellMethod(value = "registration user command", key = {"r", "registration"}, group = "user")
	public String registration(@ShellOption(defaultValue = "name", help = "Please enter you name") String name,
							   @ShellOption(defaultValue = "surname", help = "Please enter you surname") String surname) {
		this.user = userService.createUser(name, surname);
		return localizationService.localizeMessage("user.welcome", user.getName(), user.getSurname());
	}

	@ShellMethod(value = "run test command", key = {"t", "test"})
	@ShellMethodAvailability(value = "isCommandAvailable")
	public void test() {
		testService.test(user);
	}

	private Availability isCommandAvailable() {
		if (!hasAuthorized()) {
			return Availability.unavailable(localizationService.localizeMessage("user.register"));
		}
		return Availability.available();
	}

	private boolean hasAuthorized() {
		return user != null;
	}
}
