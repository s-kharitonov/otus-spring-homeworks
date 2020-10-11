package ru.otus.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.dao.CommentsDao;
import ru.otus.domain.BookComment;
import ru.otus.exceptions.CommentServiceException;
import ru.otus.validators.FieldValidator;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CommentsServiceImpl implements CommentsService {

	private final CommentsDao commentsDao;
	private final FieldValidator fieldValidator;

	public CommentsServiceImpl(final CommentsDao commentsDao,
							   final FieldValidator fieldValidator) {
		this.commentsDao = commentsDao;
		this.fieldValidator = fieldValidator;
	}

	@Override
	@Transactional
	public void save(final BookComment bookComment) {
		checkCommentOrThrow(bookComment);
		commentsDao.save(bookComment);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<BookComment> getById(final long id) {
		return commentsDao.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<BookComment> getAll() {
		return commentsDao.findAll();
	}

	@Override
	@Transactional
	public boolean removeById(final long id) {
		return commentsDao.removeById(id);
	}

	private void checkCommentOrThrow(final BookComment bookComment) {
		if (Objects.isNull(bookComment) || !fieldValidator.validate(bookComment)) {
			throw new CommentServiceException(String.format("comment: %s is invalid!", bookComment));
		}
	}
}
