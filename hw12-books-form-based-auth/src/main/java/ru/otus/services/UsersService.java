package ru.otus.services;

import ru.otus.domain.User;

import java.util.Optional;

public interface UsersService {
	Optional<User> getByUsername(String username);
}
