package ru.otus.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.dao.AuthorsRepository;
import ru.otus.domain.Author;
import ru.otus.exceptions.AuthorsServiceException;
import ru.otus.validators.FieldValidator;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AuthorsServiceImpl implements AuthorsService {

	private final AuthorsRepository authorsRepository;
	private final FieldValidator fieldValidator;

	public AuthorsServiceImpl(final AuthorsRepository authorsRepository,
							  final FieldValidator fieldValidator) {
		this.authorsRepository = authorsRepository;
		this.fieldValidator = fieldValidator;
	}

	@Override
	@Transactional
	public Author save(final Author author) {
		checkAuthorOrThrow(author);
		return authorsRepository.save(author);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Author> getById(final long id) {
		return authorsRepository.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Author> getAll() {
		return authorsRepository.findAll();
	}

	@Override
	@Transactional
	public void deleteById(final long id) {
		authorsRepository.deleteById(id);
	}

	private void checkAuthorOrThrow(final Author author) {
		if (Objects.isNull(author) || !fieldValidator.validate(author)) {
			throw new AuthorsServiceException(String.format("author: %s is invalid!", author));
		}
	}
}
