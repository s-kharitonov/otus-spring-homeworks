package ru.otus.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.dao.GenresRepository;
import ru.otus.domain.Genre;
import ru.otus.exceptions.GenresServiceException;
import ru.otus.validators.FieldValidator;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class GenresServiceImpl implements GenresService {

	private final GenresRepository genresRepository;
	private final FieldValidator fieldValidator;

	public GenresServiceImpl(final GenresRepository genresRepository,
							 final FieldValidator fieldValidator) {
		this.genresRepository = genresRepository;
		this.fieldValidator = fieldValidator;
	}

	@Override
	@Transactional
	public Genre save(final Genre genre) {
		checkGenreOrThrow(genre);
		return genresRepository.save(genre);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Genre> getById(final long id) {
		return genresRepository.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Genre> getAll() {
		return genresRepository.findAll();
	}

	@Override
	@Transactional
	public void deleteById(final long id) {
		genresRepository.deleteById(id);
	}

	private void checkGenreOrThrow(final Genre genre) {
		if (Objects.isNull(genre) || !fieldValidator.validate(genre)) {
			throw new GenresServiceException(String.format("genre: %s is invalid!", genre));
		}
	}
}
