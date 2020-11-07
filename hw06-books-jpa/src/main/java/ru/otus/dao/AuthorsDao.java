package ru.otus.dao;

import ru.otus.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorsDao {
	void save(Author author);

	Optional<Author> findById(long id);

	List<Author> findAll();

	boolean remove(long id);
}
