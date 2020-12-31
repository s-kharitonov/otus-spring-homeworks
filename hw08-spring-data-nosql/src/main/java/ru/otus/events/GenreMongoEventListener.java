package ru.otus.events;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;
import ru.otus.domain.Genre;
import ru.otus.exсeptions.NotFoundStrategy;
import ru.otus.services.BooksService;
import ru.otus.strategies.DeleteBookByGenreStrategy;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Component
public class GenreMongoEventListener extends AbstractMongoEventListener<Genre> {

	private final BooksService booksService;
	private final Map<String, DeleteBookByGenreStrategy> deleteStrategyByFieldName;

	public GenreMongoEventListener(final BooksService booksService,
								   @Qualifier("deleteBookByGenreStrategy")
								   final Map<String, DeleteBookByGenreStrategy> deleteStrategyByFieldName) {
		this.booksService = booksService;
		this.deleteStrategyByFieldName = deleteStrategyByFieldName;
	}

	@Override
	public void onAfterDelete(final AfterDeleteEvent<Genre> event) {
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
	public void onAfterSave(final AfterSaveEvent<Genre> event) {
		super.onAfterSave(event);
		final var genre = event.getSource();
		final var genreId = genre.getId();
		final var books = booksService.getByGenreId(genreId);

		if (!books.isEmpty()) {
			books.forEach(book -> book.setGenre(genre));
			booksService.saveAll(books);
		}
	}
}
