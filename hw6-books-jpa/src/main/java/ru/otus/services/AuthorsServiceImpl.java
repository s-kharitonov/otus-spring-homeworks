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
	public void saveAuthor(final Author author) {
		checkAuthorOrThrow(author);
		authorsDao.saveAuthor(author);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Author> getAuthorById(final long id) {
		return authorsDao.findAuthorById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Author> getAllAuthors() {
		return authorsDao.findAllAuthors();
	}

	@Override
	@Transactional
	public boolean removeAuthor(final long id) {
		return authorsDao.removeAuthor(id);
	}

	private void checkAuthorOrThrow(final Author author) {
		if (Objects.isNull(author) || !fieldValidator.validate(author)) {
			throw new AuthorsServiceException(String.format("author: %s is invalid!", author));
		}
	}
}
