package ru.otus.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.otus.dao.UserDAO;
import ru.otus.domain.User;
import ru.otus.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	private final UserDAO userDAO;

	public UserServiceImpl(final UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	@Override
	public User createUser(String name, String surname) {

		if (StringUtils.isEmpty(name) || StringUtils.isEmpty(surname)) {
			throw new IllegalArgumentException("please correct your name!");
		}

		final User user = new User(name, surname);

		userDAO.saveUser(user);

		return user;
	}

	@Override
	public User getUserByName(final String name) {
		return userDAO.findByName(name)
				.orElse(null);
	}
}
