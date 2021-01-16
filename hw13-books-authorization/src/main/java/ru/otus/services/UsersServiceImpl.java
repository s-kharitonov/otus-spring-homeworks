package ru.otus.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.domain.User;
import ru.otus.exceptions.UserServiceException;
import ru.otus.repositories.UsersRepository;

import java.util.Objects;
import java.util.Optional;

@Service
public class UsersServiceImpl implements UsersService{

	private final UsersRepository usersRepository;

	public UsersServiceImpl(final UsersRepository usersRepository) {
		this.usersRepository = usersRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<User> getByUsername(final String username) {
		checkUsernameOrThrow(username);
		return usersRepository.findByUsername(username);
	}

	private void checkUsernameOrThrow(final String username) {
		if (Objects.isNull(username) || username.isBlank()) {
			throw new UserServiceException("username is null or empty!");
		}
	}
}
