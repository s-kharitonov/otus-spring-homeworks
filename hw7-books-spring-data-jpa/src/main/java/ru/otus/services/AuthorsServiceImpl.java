package ru.otus.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.dao.AuthorsDao;
import ru.otus.domain.Author;
import ru.otus.exceptions.AuthorsServiceException;
import ru.otus.validators.FieldValidator;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AuthorsServiceImpl implements AuthorsService {

	private final AuthorsDao authorsDao;
	private final FieldValidator fieldValidator;

	public AuthorsServiceImpl(final AuthorsDao authorsDao,
							  final FieldValidator fieldValidator) {
		this.authorsDao = authorsDao;
		this.fieldValidator = fieldValidator;
	}

	@Override
	@Transactional
	public void save(final Author author) {
		checkAuthorOrThrow(author);
		authorsDao.save(author);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Author> getById(final long id) {
		return authorsDao.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Author> getAll() {
		return authorsDao.findAll();
	}

	@Override
	@Transactional
	public boolean removeById(final long id) {
		return authorsDao.remove(id);
	}

	private void checkAuthorOrThrow(final Author author) {
		if (Objects.isNull(author) || !fieldValidator.validate(author)) {
			throw new AuthorsServiceException(String.format("author: %s is invalid!", author));
		}
	}
}
