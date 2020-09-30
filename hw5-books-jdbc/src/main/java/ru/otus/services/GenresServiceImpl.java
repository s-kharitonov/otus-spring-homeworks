package ru.otus.services;

import org.springframework.stereotype.Service;
import ru.otus.dao.GenresDao;
import ru.otus.domain.Genre;
import ru.otus.exceptions.GenresServiceException;
import ru.otus.validators.FieldValidator;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class GenresServiceImpl implements GenresService {

	private final GenresDao genresDao;
	private final FieldValidator fieldValidator;

	public GenresServiceImpl(final GenresDao genresDao,
							 final FieldValidator fieldValidator) {
		this.genresDao = genresDao;
		this.fieldValidator = fieldValidator;
	}

	@Override
	public Optional<Long> saveGenre(final Genre genre) {
		checkGenreOrThrow(genre);
		return genresDao.saveGenre(genre);
	}

	@Override
	public Optional<Genre> getGenreById(final long id) {
		return genresDao.findGenreById(id);
	}

	@Override
	public List<Genre> getAllGenres() {
		return genresDao.findAllGenres();
	}

	@Override
	public boolean removeGenre(final long id) {
		return genresDao.removeGenre(id);
	}

	@Override
	public boolean updateGenre(final Genre genre) {
		checkGenreOrThrow(genre);
		return genresDao.updateGenre(genre);
	}

	private void checkGenreOrThrow(final Genre genre) {
		if (Objects.isNull(genre) || !fieldValidator.validate(genre)) {
			throw new GenresServiceException(String.format("genre: %s is invalid!", genre));
		}
	}
}
