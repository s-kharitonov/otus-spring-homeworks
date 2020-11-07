package ru.otus.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.dao.BooksDao;
import ru.otus.domain.Book;
import ru.otus.exceptions.BooksServiceException;
import ru.otus.validators.FieldValidator;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BooksServiceImpl implements BooksService {

	private final BooksDao booksDao;
	private final FieldValidator fieldValidator;

	public BooksServiceImpl(final BooksDao booksDao,
							final FieldValidator fieldValidator) {
		this.booksDao = booksDao;
		this.fieldValidator = fieldValidator;
	}

	@Override
	@Transactional
	public void save(final Book book) {
		checkBookOrThrow(book);
		booksDao.save(book);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Book> getById(final long id) {
		return booksDao.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Book> getAll() {
		return booksDao.findAll();
	}

	@Override
	@Transactional
	public boolean removeById(final long id) {
		return booksDao.removeById(id);
	}

	private void checkBookOrThrow(final Book book) {
		if (Objects.isNull(book) || !fieldValidator.validate(book)) {
			throw new BooksServiceException(String.format("book: %s is invalid!", book));
		}
	}
}
