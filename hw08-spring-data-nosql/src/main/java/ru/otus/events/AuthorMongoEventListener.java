package ru.otus.events;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;
import ru.otus.domain.Author;
import ru.otus.services.BooksService;

import java.util.Objects;

@Component
public class AuthorMongoEventListener extends AbstractMongoEventListener<Author> {

	private final BooksService booksService;

	public AuthorMongoEventListener(final BooksService booksService) {
		this.booksService = booksService;
	}

	@Override
	public void onAfterDelete(final AfterDeleteEvent<Author> event) {
		super.onAfterDelete(event);
		final var source = event.getSource();
		final var authorId = source.get("_id").toString();

		if (!Objects.isNull(authorId)) {
			booksService.deleteByAuthorId(authorId);
		}
	}

	@Override
	public void onAfterSave(final AfterSaveEvent<Author> event) {
		super.onAfterSave(event);
		final var author = event.getSource();
		final var authorId = author.getId();
		final var books = booksService.getByAuthorId(authorId);

		if (!books.isEmpty()) {
			books.forEach(book -> book.setAuthor(author));
			booksService.saveAll(books);
		}
	}
}
