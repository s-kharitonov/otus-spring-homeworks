package ru.otus.services;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.domain.BookComment;
import ru.otus.exeptions.BookCommentsServiceException;
import ru.otus.repositories.BookCommentsRepository;
import ru.otus.validators.FieldValidator;

import java.util.Collections;
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
	public BookComment save(final BookComment comment) {
		checkBookCommentsOrThrow(Collections.singletonList(comment));
		return bookCommentsRepository.save(comment);
	}

	@Override
	@Transactional
	public List<BookComment> saveAll(final List<BookComment> comments) {
		checkBookCommentsOrThrow(comments);
		return bookCommentsRepository.saveAll(comments);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<BookComment> getById(final String id) {
		checkIdsOrThrow(Collections.singletonList(id));
		return bookCommentsRepository.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<BookComment> getByBookId(final String id) {
		checkIdsOrThrow(Collections.singletonList(id));
		return bookCommentsRepository.findAllByBook_Id(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<BookComment> getByBooksIds(final List<String> ids) {
		checkIdsOrThrow(ids);
		return bookCommentsRepository.findAllByBook_IdIn(ids);
	}

	@Override
	@Transactional(readOnly = true)
	public List<BookComment> getAll() {
		return bookCommentsRepository.findAll();
	}

	@Override
	@Transactional
	public void deleteById(final String id) {
		checkIdsOrThrow(Collections.singletonList(id));
		bookCommentsRepository.deleteById(id);
	}

	@Override
	@Transactional
	public void deleteByBookId(final String id) {
		checkIdsOrThrow(Collections.singletonList(id));
		bookCommentsRepository.deleteAllByBook_Id(id);
	}

	@Override
	@Transactional
	public void deleteByGenreId(final String id) {
		checkIdsOrThrow(Collections.singletonList(id));
		bookCommentsRepository.deleteAllByBook_Genre_id(id);
	}

	@Override
	@Transactional
	public void deleteByAuthorId(final String id) {
		checkIdsOrThrow(Collections.singletonList(id));
		bookCommentsRepository.deleteAllByBook_Author_id(id);
	}

	private void checkIdsOrThrow(final List<String> ids) {
		if (Objects.isNull(ids)) {
			throw new BookCommentsServiceException("book comment id must not be null!");
		}

		if (hasInvalidIds(ids)) {
			throw new BookCommentsServiceException("book comment id must not be null or empty!");
		}
	}

	private void checkBookCommentsOrThrow(final List<BookComment> comments) {
		if (Objects.isNull(comments)) {
			throw new BookCommentsServiceException("book comment must not be null!");
		}

		final boolean hasInvalidComments = hasInvalidComments(comments);

		if (hasInvalidComments) {
			throw new BookCommentsServiceException("book comment hasn't invalid fields!");
		}
	}

	private boolean hasInvalidIds(final List<String> ids) {
		return ids.stream().anyMatch(StringUtils::isBlank);
	}

	private boolean hasInvalidComments(final List<BookComment> comments) {
		return comments.stream()
				.anyMatch(comment -> Objects.isNull(comment) || !fieldValidator.validate(comment));
	}
}
