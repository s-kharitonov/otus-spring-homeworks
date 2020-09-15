package ru.otus.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.dao.UserDAO;
import ru.otus.domain.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserInMemoryDAOUnitTest {

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

	@Test
	@DisplayName("should return empty optional when user not found")
	public void shouldReturnEmptyResultWhenUserNotFound() {
		assertEquals(Optional.empty(), userDAO.findByName(""));
	}

	@Test
	@DisplayName("should return result when user name is null")
	public void shouldReturnResultForEmptyName() {
		assertEquals(Optional.empty(), userDAO.findByName(null));
	}

	@Test
	@DisplayName("should throw NPE when user is null")
	public void  shouldThrowExceptionWhenUserIsNull() {
		assertThrows(NullPointerException.class, () -> userDAO.saveUser(null));
	}
}