package ru.otus.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.dao.BookCommentsDao;
import ru.otus.domain.BookComment;
import ru.otus.exceptions.BookCommentsServiceException;
import ru.otus.validators.FieldValidator;

import java.util.Objects;

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
		checkOrThrow(bookComment);
		bookCommentsDao.save(bookComment);
	}

	private void checkOrThrow(final BookComment bookComment) {
		if (Objects.isNull(bookComment) || !fieldValidator.validate(bookComment)) {
			throw new BookCommentsServiceException(String.format("bookComment: %s is invalid!", bookComment));
		}
	}
}
