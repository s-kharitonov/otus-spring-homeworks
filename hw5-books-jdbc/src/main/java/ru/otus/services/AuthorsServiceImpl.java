package ru.otus.services;

import org.springframework.stereotype.Service;
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
	private final LocalizationService localizationService;
	private final FieldValidator fieldValidator;

	public AuthorsServiceImpl(final AuthorsDao authorsDao,
							  final LocalizationService localizationService,
							  final FieldValidator fieldValidator) {
		this.authorsDao = authorsDao;
		this.localizationService = localizationService;
		this.fieldValidator = fieldValidator;
	}

	@Override
	public Optional<Long> createAuthor(final Author author) {
		checkAuthorOrThrow(author);
		return authorsDao.saveAuthor(author);
	}

	@Override
	public Optional<Author> getAuthorById(final long id) {
		return authorsDao.findAuthorById(id);
	}

	@Override
	public List<Author> getAllAuthors() {
		return authorsDao.findAllAuthors();
	}

	@Override
	public void removeAuthor(final long id) {
		authorsDao.removeAuthor(id);
	}

	@Override
	public void updateAuthor(final Author author) {
		checkAuthorOrThrow(author);
		authorsDao.updateAuthor(author);
	}

	private void checkAuthorOrThrow(final Author author) {
		if (Objects.isNull(author) || !fieldValidator.validate(author)) {
			throw new AuthorsServiceException(
					localizationService.localizeMessage("invalid.author", author)
			);
		}
	}
}