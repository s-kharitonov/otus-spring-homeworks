package ru.otus.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import ru.otus.dao.UserDAO;
import ru.otus.services.UserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class UserServiceImplPositiveUnitTest {

	private static final String SURNAME = "kharitonov";
	private static final String NAME = "sergey";

	private InOrder inOrder;
	private UserDAO userDAO;
	private UserService userService;

	@BeforeEach
	void setUp() {
		userDAO = mock(UserDAO.class);
		userService = new UserServiceImpl(userDAO);
		inOrder = inOrder(userDAO);
	}

	@Test
	@DisplayName("should call DAO for saving user when user will be created")
	public void shouldSaveUserWhenUserWillCreated() {
		var user = userService.createUser(NAME, SURNAME);
		inOrder.verify(userDAO, times(1)).saveUser(user);
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

		given(userDAO.findByName(NAME)).willReturn(Optional.of(user));
		assertEquals(user, userService.getUserByName(NAME));
	}
}