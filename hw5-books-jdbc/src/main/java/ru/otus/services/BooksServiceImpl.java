package ru.otus.services;

import org.springframework.stereotype.Service;
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
	public Optional<Long> createBook(final Book book) {
		checkBookOrThrow(book);
		return booksDao.saveBook(book);
	}

	@Override
	public Optional<Book> getBookById(final long id) {
		return booksDao.findBookById(id);
	}

	@Override
	public List<Book> getAllBooks() {
		return booksDao.findAllBooks();
	}

	@Override
	public boolean removeBook(final long id) {
		return booksDao.removeBook(id);
	}

	@Override
	public boolean updateBook(final Book book) {
		checkBookOrThrow(book);
		return booksDao.updateBook(book);
	}

	private void checkBookOrThrow(final Book book) {
		if (Objects.isNull(book) || !fieldValidator.validate(book)) {
			throw new BooksServiceException(String.format("book: %s is invalid!", book));
		}
	}
}
