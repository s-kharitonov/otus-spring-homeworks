package ru.otus.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.otus.dao.UserDao;
import ru.otus.domain.User;
import ru.otus.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	private final UserDao userDao;

	public UserServiceImpl(final UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public User createUser(String name, String surname) {

		if (StringUtils.isEmpty(name) || StringUtils.isEmpty(surname)) {
			throw new IllegalArgumentException("please correct your name!");
		}

		final User user = new User(name, surname);

		userDao.saveUser(user);

		return user;
	}

	@Override
	public User getUserByName(final String name) {
		return userDao.findByName(name)
				.orElse(null);
	}
}
