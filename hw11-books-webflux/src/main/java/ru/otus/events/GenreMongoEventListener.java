package ru.otus.events;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;
import ru.otus.commands.DeleteEntityCommand;
import ru.otus.domain.Genre;
import ru.otus.repositories.BooksRepository;

import javax.annotation.Nonnull;

@Component
public class GenreMongoEventListener extends AbstractMongoEventListener<Genre> {

	private final BooksRepository booksRepository;
	private final DeleteEntityCommand deleteEntityCommand;

	public GenreMongoEventListener(final BooksRepository booksRepository,
								   final DeleteEntityCommand deleteEntityCommand) {
		this.booksRepository = booksRepository;
		this.deleteEntityCommand = deleteEntityCommand;
	}

	@Override
	public void onAfterDelete(@Nonnull final AfterDeleteEvent<Genre> event) {
		super.onAfterDelete(event);
		final var collectionName = event.getCollectionName();
		final var source = event.getSource();

		deleteEntityCommand.delete(collectionName, source);
	}

	@Override
	public void onAfterSave(@Nonnull final AfterSaveEvent<Genre> event) {
		super.onAfterSave(event);
		final var genre = event.getSource();
		final var genreId = genre.getId();

		booksRepository.findAllByGenre_Id(genreId)
				.doOnNext(book -> book.setGenre(genre))
				.collectList()
				.doOnNext(booksRepository::saveAll)
				.subscribe();
	}
}
