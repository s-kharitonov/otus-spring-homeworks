package ru.otus.services;

import ru.otus.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenresService {
	void save(Genre genre);

	Optional<Genre> getById(long id);

	List<Genre> getAll();

	boolean removeById(long id);
}
