package ru.otus.services;

import ru.otus.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorsService {
	Optional<Long> createAuthor(Author author);

	Optional<Author> getAuthorById(long id);

	List<Author> getAllAuthors();

	void removeAuthor(long id);

	void updateAuthor(Author author);
}
