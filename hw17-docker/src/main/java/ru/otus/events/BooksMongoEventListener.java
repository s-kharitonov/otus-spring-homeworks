package ru.otus.events;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;
import ru.otus.domain.Book;
import ru.otus.domain.BookComment;
import ru.otus.repositories.BookCommentsRepository;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class BooksMongoEventListener extends AbstractMongoEventListener<List<Book>> {

	private final BookCommentsRepository commentsRepository;

	public BooksMongoEventListener(final BookCommentsRepository commentsRepository) {
		this.commentsRepository = commentsRepository;
	}

	@Override
	public void onAfterSave(@Nonnull final AfterSaveEvent<List<Book>> event) {
		super.onAfterSave(event);
		final var books = event.getSource();
		final var booksIds = books.stream()
				.map(Book::getId)
				.collect(Collectors.toList());
		final var bookById = books.stream()
				.collect(Collectors.toMap(Book::getId, book -> book));

		commentsRepository.findAllByBook_IdIn(booksIds)
				.doOnNext(comment -> fillCommentWithBooks(bookById, comment))
				.collectList()
				.doOnNext(commentsRepository::saveAll)
				.subscribe();
	}

	private void fillCommentWithBooks(final Map<String, Book> bookById, final BookComment comment) {
		final var bookForUpdate = bookById.get(comment.getBook().getId());

		comment.setBook(bookForUpdate);
	}
}
