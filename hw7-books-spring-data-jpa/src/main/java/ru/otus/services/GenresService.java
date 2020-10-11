package ru.otus.services;

import ru.otus.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenresService {
	Genre save(Genre genre);

	Optional<Genre> getById(long id);

	List<Genre> getAll();

	void deleteById(long id);
}
