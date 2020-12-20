package ru.otus.services;

import ru.otus.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorsService {
	Author save(Author author);

	Optional<Author> getById(String id);

	List<Author> getAll();

	void deleteById(String id);
}
