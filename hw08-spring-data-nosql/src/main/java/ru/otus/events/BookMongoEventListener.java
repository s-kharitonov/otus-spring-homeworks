package ru.otus.events;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;
import ru.otus.domain.Book;
import ru.otus.services.BookCommentsService;

import java.util.Objects;

@Component
public class BookMongoEventListener extends AbstractMongoEventListener<Book> {

	private final BookCommentsService bookCommentsService;

	public BookMongoEventListener(final BookCommentsService bookCommentsService) {
		this.bookCommentsService = bookCommentsService;
	}

	@Override
	public void onAfterDelete(final AfterDeleteEvent<Book> event) {
		super.onAfterDelete(event);
		final var source = event.getSource();
		final var bookId = source.get("_id");
		final var genreId = source.get("genre._id");
		final var authorId = source.get("author._id");

		if (!Objects.isNull(bookId)) {
			bookCommentsService.deleteByBookId(bookId.toString());
			return;
		}

		if (!Objects.isNull(genreId)) {
			bookCommentsService.deleteByGenreId(genreId.toString());
			return;
		}

		if (!Objects.isNull(authorId)) {
			bookCommentsService.deleteByAuthorId(authorId.toString());
		}
	}

	@Override
	public void onAfterSave(final AfterSaveEvent<Book> event) {
		super.onAfterSave(event);
		final var book = event.getSource();
		final var bookId = book.getId();
		final var comments = bookCommentsService.getByBookId(bookId);

		if (!comments.isEmpty()) {
			comments.forEach((comment) -> comment.setBook(book));
			bookCommentsService.saveAll(comments);
		}
	}
}
