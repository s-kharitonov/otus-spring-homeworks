package ru.otus.services;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.domain.Book;
import ru.otus.exeptions.BooksServiceException;
import ru.otus.repositories.BooksRepository;
import ru.otus.validators.FieldValidator;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BooksServiceImpl implements BooksService{

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
		checkBooksOrThrow(Collections.singletonList(book));
		return booksRepository.save(book);
	}

	@Override
	@Transactional
	public List<Book> saveAll(final List<Book> books) {
		checkBooksOrThrow(books);
		return booksRepository.saveAll(books);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Book> getById(final String id) {
		checkIdOrThrow(id);
		return booksRepository.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Book> getByGenreId(final String id) {
		checkIdOrThrow(id);
		return booksRepository.findAllByGenre_Id(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Book> getByAuthorId(final String id) {
		checkIdOrThrow(id);
		return booksRepository.findAllByAuthor_Id(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Book> getAll() {
		return booksRepository.findAll();
	}

	@Override
	@Transactional
	public void deleteById(final String id) {
		checkIdOrThrow(id);
		booksRepository.deleteById(id);
	}

	@Override
	@Transactional
	public void deleteByAuthorId(final String id) {
		checkIdOrThrow(id);
		booksRepository.deleteAllByAuthor_Id(id);
	}

	@Override
	@Transactional
	public void deleteByGenreId(final String id) {
		checkIdOrThrow(id);
		booksRepository.deleteAllByGenre_Id(id);
	}

	private void checkBooksOrThrow(final List<Book> books) {
		if (Objects.isNull(books)) {
			throw new BooksServiceException("books must not be null!");
		}

		final boolean hasInvalidBooks = hasInvalidBooks(books);

		if (hasInvalidBooks) {
			throw new BooksServiceException("books hasn't invalid fields!");
		}
	}

	private boolean hasInvalidBooks(final List<Book> books) {
		return books.stream()
				.anyMatch(book -> Objects.isNull(book) || !fieldValidator.validate(book));
	}

	private void checkIdOrThrow(final String id) {
		if (StringUtils.isBlank(id)) {
			throw new BooksServiceException("book id must not be null or empty!");
		}
	}
}
