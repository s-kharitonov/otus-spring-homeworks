package ru.otus.dao;

import ru.otus.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenresDao {
	void saveGenre(Genre genre);

	Optional<Genre> findGenreById(long id);

	List<Genre> findAllGenres();

	boolean removeGenre(long id);
}
