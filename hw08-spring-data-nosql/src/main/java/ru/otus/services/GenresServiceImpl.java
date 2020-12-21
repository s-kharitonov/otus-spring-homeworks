package ru.otus.services;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.domain.Genre;
import ru.otus.exсeptions.GenresServiceException;
import ru.otus.repositories.GenresRepository;
import ru.otus.validators.FieldValidator;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class GenresServiceImpl implements GenresService{

	private final GenresRepository genresRepository;
	private final FieldValidator validator;

	public GenresServiceImpl(final GenresRepository genresRepository,
							 final FieldValidator validator) {
		this.genresRepository = genresRepository;
		this.validator = validator;
	}

	@Override
	@Transactional
	public Genre save(final Genre genre) {
		checkGenreOrThrow(genre);
		return genresRepository.save(genre);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Genre> getById(final String id) {
		checkGenreIdOrThrow(id);
		return genresRepository.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Genre> getAll() {
		return genresRepository.findAll();
	}

	@Override
	@Transactional
	public void deleteById(final String id) {
		checkGenreIdOrThrow(id);
		genresRepository.deleteById(id);
	}

	private void checkGenreOrThrow(final Genre genre) {
		if (Objects.isNull(genre) || !validator.validate(genre)) {
			throw new GenresServiceException("genre must not be null or invalid!");
		}
	}

	private void checkGenreIdOrThrow(final String id) {
		if (StringUtils.isBlank(id)) {
			throw new GenresServiceException("genre id must not be null or empty!");
		}
	}
}
