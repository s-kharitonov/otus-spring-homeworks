package ru.otus.services;

import ru.otus.domain.User;

public interface UserService {
	User createUser(String name, String surname);

	User getUserByName(String name);
}
