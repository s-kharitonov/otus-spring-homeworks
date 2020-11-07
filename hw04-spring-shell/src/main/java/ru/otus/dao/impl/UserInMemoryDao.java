package ru.otus.dao.impl;

import org.springframework.stereotype.Repository;
import ru.otus.dao.UserDao;
import ru.otus.domain.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserInMemoryDao implements UserDao {

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
