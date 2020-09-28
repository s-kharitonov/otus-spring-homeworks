package ru.otus.services;

import org.springframework.stereotype.Service;
import ru.otus.dao.BooksDao;
import ru.otus.domain.Book;
import ru.otus.domain.Constants;
import ru.otus.exceptions.BooksServiceException;
import ru.otus.validators.FieldValidator;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BooksServiceImpl implements BooksService {

	private final BooksDao booksDao;
	private final LocalizationService localizationService;
	private final FieldValidator fieldValidator;

	public BooksServiceImpl(final BooksDao booksDao,
							final LocalizationService localizationService,
							final FieldValidator fieldValidator) {
		this.booksDao = booksDao;
		this.localizationService = localizationService;
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
	public void removeBook(final long id) {
		booksDao.removeBook(id);
	}

	@Override
	public void updateBook(final Book book) {
		checkBookOrThrow(book);
		booksDao.updateBook(book);
	}

	private void checkBookOrThrow(final Book book) {
		if (Objects.isNull(book) || !fieldValidator.validate(book)) {
			throw new BooksServiceException(
					localizationService.localizeMessage(Constants.INVALID_BOOK_MSG_KEY, book)
			);
		}
	}
}
