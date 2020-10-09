package ru.otus.services.facades;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.domain.BookComment;
import ru.otus.domain.Comment;
import ru.otus.services.BookCommentsService;
import ru.otus.services.BooksService;
import ru.otus.services.CommentsService;

@Service
public class BookCommentsFacadeImpl implements BookCommentsFacade {

	private final CommentsService commentsService;
	private final BooksService booksService;
	private final BookCommentsService bookCommentsService;

	public BookCommentsFacadeImpl(final CommentsService commentsService,
								  final BooksService booksService,
								  final BookCommentsService bookCommentsService) {
		this.commentsService = commentsService;
		this.booksService = booksService;
		this.bookCommentsService = bookCommentsService;
	}

	@Override
	@Transactional
	public void save(final long bookId, final Comment comment) {
		var book = booksService.getById(bookId).orElseThrow();

		commentsService.save(comment);
		bookCommentsService.save(new BookComment(book, comment));
	}
}
