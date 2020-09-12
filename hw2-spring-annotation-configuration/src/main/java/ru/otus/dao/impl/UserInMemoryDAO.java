package ru.otus.dao.impl;

import org.springframework.stereotype.Component;
import ru.otus.dao.UserDAO;
import ru.otus.domain.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class UserInMemoryDAO implements UserDAO {

	private final Map<String, User> users = new HashMap<>();

	@Override
	public Optional<User> findByName(final String name) {
		return Optional.ofNullable(users.get(name));
	}

	@Override
	public void saveUser(final User user) {
		users.put(user.getName(), user);
	}
}
