package ru.otus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.domain.User;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<User, Long> {

	Optional<User> findByUsername(String username);
}
