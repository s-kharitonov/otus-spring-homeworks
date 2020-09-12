package ru.otus.dao;

import ru.otus.domain.User;

import java.util.Optional;

public interface UserDAO {
	Optional<User> findByName(String name);

	void saveUser(User user);
}
