package ru.otus.services;

import ru.otus.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenresService {
	Optional<Long> saveGenre(Genre genre);

	Optional<Genre> getGenreById(long id);

	List<Genre> getAllGenres();

	boolean removeGenre(long id);

	boolean updateGenre(Genre genre);
}
