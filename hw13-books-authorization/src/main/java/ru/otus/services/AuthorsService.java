package ru.otus.services;

import ru.otus.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorsService {
	Author save(Author author);

	Optional<Author> getById(long id);

	List<Author> getAll();

	void deleteById(long id);
}
