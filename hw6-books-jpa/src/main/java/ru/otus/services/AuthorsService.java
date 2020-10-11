package ru.otus.services;

import ru.otus.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorsService {
	void save(Author author);

	Optional<Author> getById(long id);

	List<Author> getAll();

	boolean removeById(long id);
}
