package ru.otus.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.dao.BookCommentsDao;
import ru.otus.domain.BookComment;
import ru.otus.exceptions.BookCommentsServiceException;
import ru.otus.validators.FieldValidator;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BookCommentsServiceImpl implements BookCommentsService {

	private final BookCommentsDao bookCommentsDao;
	private final FieldValidator fieldValidator;

	public BookCommentsServiceImpl(final BookCommentsDao bookCommentsDao,
								   final FieldValidator fieldValidator) {
		this.bookCommentsDao = bookCommentsDao;
		this.fieldValidator = fieldValidator;
	}

	@Override
	@Transactional
	public void save(final BookComment bookComment) {
		checkCommentOrThrow(bookComment);
		bookCommentsDao.save(bookComment);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<BookComment> getById(final long id) {
		return bookCommentsDao.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<BookComment> getAll() {
		return bookCommentsDao.findAll();
	}

	@Override
	@Transactional
	public boolean removeById(final long id) {
		return bookCommentsDao.removeById(id);
	}

	private void checkCommentOrThrow(final BookComment bookComment) {
		if (Objects.isNull(bookComment) || !fieldValidator.validate(bookComment)) {
			throw new BookCommentsServiceException(String.format("comment: %s is invalid!", bookComment));
		}
	}
}
