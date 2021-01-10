package ru.otus.events;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;
import ru.otus.commands.DeleteEntityCommand;
import ru.otus.domain.Author;
import ru.otus.services.BooksService;

import javax.annotation.Nonnull;

@Component
public class AuthorMongoEventListener extends AbstractMongoEventListener<Author> {

	private final BooksService booksService;
	private final DeleteEntityCommand deleteEntityCommand;

	public AuthorMongoEventListener(final BooksService booksService,
									final DeleteEntityCommand deleteEntityCommand) {
		this.booksService = booksService;
		this.deleteEntityCommand = deleteEntityCommand;
	}

	@Override
	public void onAfterDelete(@Nonnull final AfterDeleteEvent<Author> event) {
		super.onAfterDelete(event);
		final var collectionName = event.getCollectionName();
		final var source = event.getSource();

		deleteEntityCommand.delete(collectionName, source);
	}

	@Override
	public void onAfterSave(@Nonnull final AfterSaveEvent<Author> event) {
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
