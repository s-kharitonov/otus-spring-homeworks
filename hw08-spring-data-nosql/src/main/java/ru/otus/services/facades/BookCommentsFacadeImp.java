package ru.otus.services.facades;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.domain.BookComment;
import ru.otus.domain.BookCommentCandidate;
import ru.otus.exсeptions.BookCommentsServiceException;
import ru.otus.services.BookCommentsService;
import ru.otus.services.BooksService;

import java.util.List;
import java.util.Optional;

@Service
public class BookCommentsFacadeImp implements BookCommentsFacade{

	private final BookCommentsService bookCommentsService;
	private final BooksService booksService;

	public BookCommentsFacadeImp(final BookCommentsService bookCommentsService,
								 final BooksService booksService) {
		this.bookCommentsService = bookCommentsService;
		this.booksService = booksService;
	}

	@Override
	@Transactional
	public BookComment save(final BookCommentCandidate commentCandidate) {
		final var book = booksService.getById(commentCandidate.getBookId())
				.orElseThrow(() -> new BookCommentsServiceException("book not found!"));
		final var comment = new BookComment.Builder()
				.id(commentCandidate.getCommentId())
				.text(commentCandidate.getText())
				.book(book)
				.build();

		return bookCommentsService.save(comment);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<BookComment> getById(final String id) {
		return bookCommentsService.getById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<BookComment> getAll() {
		return bookCommentsService.getAll();
	}

	@Override
	@Transactional
	public void deleteById(final String id) {
		bookCommentsService.deleteById(id);
	}
}
