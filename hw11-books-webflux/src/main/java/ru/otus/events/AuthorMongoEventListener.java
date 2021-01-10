package ru.otus.events;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;
import ru.otus.commands.DeleteEntityCommand;
import ru.otus.domain.Author;
import ru.otus.repositories.BooksRepository;

import javax.annotation.Nonnull;

@Component
public class AuthorMongoEventListener extends AbstractMongoEventListener<Author> {

	private final BooksRepository booksRepository;
	private final DeleteEntityCommand deleteEntityCommand;

	public AuthorMongoEventListener(final BooksRepository booksRepository,
									final DeleteEntityCommand deleteEntityCommand) {
		this.booksRepository = booksRepository;
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

		booksRepository.findAllByAuthor_Id(authorId)
				.doOnNext(book -> book.setAuthor(author))
				.collectList()
				.doOnNext(booksRepository::saveAll)
				.subscribe();
	}
}
