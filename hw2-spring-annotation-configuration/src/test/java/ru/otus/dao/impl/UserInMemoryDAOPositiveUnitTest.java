package ru.otus.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.dao.UserDAO;
import ru.otus.domain.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserInMemoryDAOPositiveUnitTest {

	private UserDAO userDAO;

	@BeforeEach
	void setUp() {
		this.userDAO = new UserInMemoryDAO();
	}

	@Test
	@DisplayName("should return saved user by name")
	public void shouldReturnSavedUserByName() {
		final var user = new User("sergey", "kharitonov");
		userDAO.saveUser(user);
		assertEquals(user, userDAO.findByName("sergey").orElseThrow());
	}
}