package ru.otus.services;

import ru.otus.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorsService {
	void saveAuthor(Author author);

	Optional<Author> getAuthorById(long id);

	List<Author> getAllAuthors();

	boolean removeAuthor(long id);
}
