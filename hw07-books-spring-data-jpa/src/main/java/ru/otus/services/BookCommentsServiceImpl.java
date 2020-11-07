package ru.otus.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.domain.BookComment;
import ru.otus.exceptions.BookCommentsServiceException;
import ru.otus.repositories.BookCommentsRepository;
import ru.otus.validators.FieldValidator;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BookCommentsServiceImpl implements BookCommentsService {

	private final BookCommentsRepository bookCommentsRepository;
	private final FieldValidator fieldValidator;

	public BookCommentsServiceImpl(final BookCommentsRepository bookCommentsRepository,
								   final FieldValidator fieldValidator) {
		this.bookCommentsRepository = bookCommentsRepository;
		this.fieldValidator = fieldValidator;
	}

	@Override
	@Transactional
	public BookComment save(final BookComment bookComment) {
		checkCommentOrThrow(bookComment);
		return bookCommentsRepository.save(bookComment);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<BookComment> getById(final long id) {
		return bookCommentsRepository.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<BookComment> getAll() {
		return bookCommentsRepository.findAll();
	}

	@Override
	@Transactional
	public void deleteById(final long id) {
		bookCommentsRepository.deleteById(id);
	}

	private void checkCommentOrThrow(final BookComment bookComment) {
		if (Objects.isNull(bookComment) || !fieldValidator.validate(bookComment)) {
			throw new BookCommentsServiceException(String.format("comment: %s is invalid!", bookComment));
		}
	}
}
