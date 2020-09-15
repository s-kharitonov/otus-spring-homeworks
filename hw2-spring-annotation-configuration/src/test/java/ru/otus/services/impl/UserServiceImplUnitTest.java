package ru.otus.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InOrder;
import ru.otus.dao.UserDao;
import ru.otus.services.UserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class UserServiceImplUnitTest {

	private static final String SURNAME = "kharitonov";
	private static final String NAME = "sergey";

	private InOrder inOrder;
	private UserDao userDao;
	private UserService userService;

	@BeforeEach
	void setUp() {
		userDao = mock(UserDao.class);
		userService = new UserServiceImpl(userDao);
		inOrder = inOrder(userDao);
	}

	@Test
	@DisplayName("should call DAO for saving user when user will be created")
	public void shouldSaveUserWhenUserWillCreated() {
		var user = userService.createUser(NAME, SURNAME);
		inOrder.verify(userDao, times(1)).saveUser(user);
	}

	@Test
	@DisplayName("should return created user")
	public void shouldCreateUser() {
		assertNotNull(userService.createUser(NAME, SURNAME));
	}

	@Test
	@DisplayName("should return user by name")
	public void shouldReturnUserByName() {
		var user = userService.createUser(NAME, SURNAME);

		given(userDao.findByName(NAME)).willReturn(Optional.of(user));
		assertEquals(user, userService.getUserByName(NAME));
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