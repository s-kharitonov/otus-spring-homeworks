package ru.otus.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.domain.User;
import ru.otus.services.LocalizationService;
import ru.otus.services.TestService;
import ru.otus.services.UserService;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;

@SpringBootTest
class TestControllerUnitTest {

	private static final String NAME = "name";
	private static final String SURNAME = "surname";

	@MockBean
	private UserService userService;
	@MockBean
	private TestService testService;
	@MockBean
	private LocalizationService localizationService;
	@Autowired
	private TestController testController;
	private InOrder inOrder;
	private User user;

	@BeforeEach
	void setUp() {
		this.inOrder = inOrder(userService, testService, localizationService);
		this.user = new User(NAME, SURNAME);
	}

	@Test
	@DisplayName("should create user using UserService")
	public void shouldCreateUser(){
		given(userService.createUser(NAME, SURNAME)).willReturn(user);
		testController.registration(NAME, SURNAME);
		inOrder.verify(userService, times(1)).createUser(NAME, SURNAME);
	}

	@Test
	@DisplayName("should print localized user welcome message")
	public void shouldPrintLocalizedWelcomeMessage() {
		given(userService.createUser(NAME, SURNAME)).willReturn(user);
		testController.registration(NAME, SURNAME);
		inOrder.verify(localizationService, times(1))
				.localizeMessage("user.welcome", user.getName(), user.getSurname());
	}

	@Test
	@DisplayName("should run user test")
	public void shouldRunTest() {
		given(userService.createUser(NAME, SURNAME)).willReturn(user);
		testController.registration(NAME, SURNAME);
		testController.test();
		inOrder.verify(testService, times(1)).test(user);
	}
}