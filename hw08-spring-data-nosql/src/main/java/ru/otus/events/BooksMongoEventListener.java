package ru.otus.events;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;
import ru.otus.domain.Book;
import ru.otus.domain.BookComment;
import ru.otus.services.BookCommentsService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class BooksMongoEventListener extends AbstractMongoEventListener<List<Book>> {

	private final BookCommentsService bookCommentsService;

	public BooksMongoEventListener(final BookCommentsService bookCommentsService) {
		this.bookCommentsService = bookCommentsService;
	}

	@Override
	public void onAfterSave(final AfterSaveEvent<List<Book>> event) {
		super.onAfterSave(event);
		final var books = event.getSource();
		final var booksIds = books.stream()
				.map(Book::getId)
				.collect(Collectors.toList());
		final var bookById = books.stream()
				.collect(Collectors.toMap(Book::getId, book -> book));
		final var comments = bookCommentsService.getByBooksIds(booksIds);

		if (!comments.isEmpty()) {
			fillCommentsWithBooks(bookById, comments);
			bookCommentsService.saveAll(comments);
		}
	}

	private void fillCommentsWithBooks(final Map<String, Book> bookById, final List<BookComment> comments) {
		comments.forEach(comment -> {
			final var bookForUpdate = bookById.get(comment.getBook().getId());

			comment.setBook(bookForUpdate);
		});
	}
}
