package ru.otus.events;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;
import ru.otus.domain.Author;
import ru.otus.exсeptions.NotFoundStrategy;
import ru.otus.services.BooksService;
import ru.otus.strategies.DeleteBookByAuthorStrategy;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Component
public class AuthorMongoEventListener extends AbstractMongoEventListener<Author> {

	private final BooksService booksService;
	private final Map<String, DeleteBookByAuthorStrategy> deleteStrategyByFieldName;

	public AuthorMongoEventListener(final BooksService booksService,
									@Qualifier("deleteBookByAuthorStrategy")
									final Map<String, DeleteBookByAuthorStrategy> deleteStrategyByFieldName) {
		this.booksService = booksService;
		this.deleteStrategyByFieldName = deleteStrategyByFieldName;
	}

	@Override
	public void onAfterDelete(final AfterDeleteEvent<Author> event) {
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
