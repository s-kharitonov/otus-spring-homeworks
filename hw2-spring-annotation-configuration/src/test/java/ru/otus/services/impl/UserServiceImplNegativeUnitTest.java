package ru.otus.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import ru.otus.dao.UserDAO;
import ru.otus.services.UserService;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class UserServiceImplNegativeUnitTest {

	private static final String SURNAME = "kharitonov";
	private static final String NAME = "sergey";

	private UserService userService;

	@BeforeEach
	void setUp() {
		userService = new UserServiceImpl(mock(UserDAO.class));
	}

	@ParameterizedTest
	@NullSource
	@EmptySource
	@DisplayName("should Throw IllegalArgumentException for empty name")
	public void shouldThrowExceptionWithEmptyName(final String name) {
		assertThrows(IllegalArgumentException.class, () -> userService.createUser(name, SURNAME));
	}

	@ParameterizedTest
	@NullSource
	@EmptySource
	@DisplayName("should Throw IllegalArgumentException for empty surname")
	public void shouldThrowExceptionWithEmptySurname(final String surname) {
		assertThrows(IllegalArgumentException.class, () -> userService.createUser(NAME, surname));
	}

	@Test
	@DisplayName("should return null when user not found!")
	public void shouldReturnNullWhenUserNotFound() {
		assertNull(userService.getUserByName(NAME));
	}
}