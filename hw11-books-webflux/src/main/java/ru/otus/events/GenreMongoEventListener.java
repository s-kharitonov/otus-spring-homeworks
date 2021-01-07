package ru.otus.events;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;
import ru.otus.domain.Genre;
import ru.otus.exceptions.NotFoundStrategy;
import ru.otus.repositories.BooksRepository;
import ru.otus.strategies.DeleteBookByGenreStrategy;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Component
public class GenreMongoEventListener extends AbstractMongoEventListener<Genre> {

	private final BooksRepository booksRepository;
	private final Map<String, DeleteBookByGenreStrategy> deleteStrategyByFieldName;

	public GenreMongoEventListener(final BooksRepository booksRepository,
								   @Qualifier("deleteBookByGenreStrategy")
								   final Map<String, DeleteBookByGenreStrategy> deleteStrategyByFieldName) {
		this.booksRepository = booksRepository;
		this.deleteStrategyByFieldName = deleteStrategyByFieldName;
	}

	@Override
	public void onAfterDelete(@Nonnull final AfterDeleteEvent<Genre> event) {
		super.onAfterDelete(event);
		final Set<Map.Entry<String, Object>> valueByFieldName = event.getSource().entrySet();

		for (Map.Entry<String, Object> valueByFieldNameEntry : valueByFieldName) {
			final var key = valueByFieldNameEntry.getKey();
			final var value = valueByFieldNameEntry.getValue();
			final var strategy = deleteStrategyByFieldName.get(key);

			if (Objects.isNull(strategy)) {
				throw new NotFoundStrategy("strategy for delete book not found!");
			}

			strategy.delete(value.toString());
		}
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
