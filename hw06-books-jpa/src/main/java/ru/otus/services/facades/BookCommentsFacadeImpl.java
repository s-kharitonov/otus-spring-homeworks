package ru.otus.services.facades;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.domain.BookComment;
import ru.otus.domain.dto.BookCommentDto;
import ru.otus.services.BookCommentsService;
import ru.otus.services.BooksService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookCommentsFacadeImpl implements BookCommentsFacade {

	private final BookCommentsService bookCommentsService;
	private final BooksService booksService;

	public BookCommentsFacadeImpl(final BookCommentsService bookCommentsService,
								  final BooksService booksService) {
		this.bookCommentsService = bookCommentsService;
		this.booksService = booksService;
	}

	@Override
	@Transactional
	public Optional<Long> save(final BookCommentDto bookComment) {
		var book = booksService.getById(bookComment.getBook().getId()).orElseThrow();
		var bookCommentForSave = new BookComment.Builder()
				.id(bookComment.getId())
				.book(book)
				.text(bookComment.getText())
				.build();

		bookCommentsService.save(bookCommentForSave);
		return Optional.ofNullable(bookCommentForSave.getId());
	}

	@Override
	@Transactional
	public Optional<BookCommentDto> getById(final long id) {
		return bookCommentsService.getById(id).map(BookCommentDto::new);
	}

	@Override
	@Transactional
	public List<BookCommentDto> getAll() {
		return bookCommentsService.getAll()
				.stream()
				.map(BookCommentDto::new)
				.collect(Collectors.toUnmodifiableList());
	}

	@Override
	@Transactional
	public boolean removeById(final long id) {
		return bookCommentsService.removeById(id);
	}
}
