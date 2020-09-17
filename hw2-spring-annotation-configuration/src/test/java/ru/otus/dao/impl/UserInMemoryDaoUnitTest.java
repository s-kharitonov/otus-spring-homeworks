package ru.otus.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.dao.UserDao;
import ru.otus.domain.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserInMemoryDaoUnitTest {

	private UserDao userDao;

	@BeforeEach
	void setUp() {
		this.userDao = new UserInMemoryDao();
	}

	@Test
	@DisplayName("should return saved user by name")
	public void shouldReturnSavedUserByName() {
		final var user = new User("sergey", "kharitonov");
		userDao.saveUser(user);
		assertEquals(user, userDao.findByName("sergey").orElseThrow());
	}

	@Test
	@DisplayName("should return empty optional when user not found")
	public void shouldReturnEmptyResultWhenUserNotFound() {
		assertEquals(Optional.empty(), userDao.findByName(""));
	}

	@Test
	@DisplayName("should return result when user name is null")
	public void shouldReturnResultForEmptyName() {
		assertEquals(Optional.empty(), userDao.findByName(null));
	}

	@Test
	@DisplayName("should throw NPE when user is null")
	public void  shouldThrowExceptionWhenUserIsNull() {
		assertThrows(NullPointerException.class, () -> userDao.saveUser(null));
	}
}