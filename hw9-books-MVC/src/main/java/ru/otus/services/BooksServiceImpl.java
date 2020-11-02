package ru.otus.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.domain.Book;
import ru.otus.exceptions.BooksServiceException;
import ru.otus.repositories.BooksRepository;
import ru.otus.validators.FieldValidator;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BooksServiceImpl implements BooksService {

	private final BooksRepository booksRepository;
	private final FieldValidator fieldValidator;

	public BooksServiceImpl(final BooksRepository booksRepository,
							final FieldValidator fieldValidator) {
		this.booksRepository = booksRepository;
		this.fieldValidator = fieldValidator;
	}

	@Override
	@Transactional
	public Book save(final Book book) {
		checkBookOrThrow(book);
		return booksRepository.save(book);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Book> getById(final long id) {
		return booksRepository.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Book> getAll() {
		return booksRepository.findAll();
	}

	@Override
	@Transactional
	public void deleteById(final long id) {
		var book = getById(id).orElseThrow(() -> new BooksServiceException(
				String.format("book with id: %s is not found!", id)
		));
		booksRepository.deleteById(book.getId());
	}

	private void checkBookOrThrow(final Book book) {
		if (Objects.isNull(book) || !fieldValidator.validate(book)) {
			throw new BooksServiceException(String.format("book: %s is invalid!", book));
		}
	}
}
