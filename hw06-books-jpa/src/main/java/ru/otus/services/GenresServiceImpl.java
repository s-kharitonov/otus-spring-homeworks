package ru.otus.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
	@Transactional
	public void save(final Genre genre) {
		checkGenreOrThrow(genre);
		genresDao.save(genre);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Genre> getById(final long id) {
		return genresDao.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Genre> getAll() {
		return genresDao.findAll();
	}

	@Override
	@Transactional
	public boolean removeById(final long id) {
		return genresDao.removeById(id);
	}

	private void checkGenreOrThrow(final Genre genre) {
		if (Objects.isNull(genre) || !fieldValidator.validate(genre)) {
			throw new GenresServiceException(String.format("genre: %s is invalid!", genre));
		}
	}
}
