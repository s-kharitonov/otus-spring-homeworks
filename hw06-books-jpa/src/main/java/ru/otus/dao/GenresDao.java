package ru.otus.dao;

import ru.otus.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenresDao {
	void save(Genre genre);

	Optional<Genre> findById(long id);

	List<Genre> findAll();

	boolean removeById(long id);
}
