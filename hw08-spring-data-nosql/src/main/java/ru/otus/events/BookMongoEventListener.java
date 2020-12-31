package ru.otus.events;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;
import ru.otus.domain.Book;
import ru.otus.exсeptions.NotFoundStrategy;
import ru.otus.services.BookCommentsService;
import ru.otus.strategies.DeleteCommentByBookStrategy;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Component
public class BookMongoEventListener extends AbstractMongoEventListener<Book> {

	private final BookCommentsService bookCommentsService;
	private final Map<String, DeleteCommentByBookStrategy> deleteStrategyByFieldName;

	public BookMongoEventListener(final BookCommentsService bookCommentsService,
								  @Qualifier("deleteCommentByBookStrategy")
								  final Map<String, DeleteCommentByBookStrategy> deleteStrategyByFieldName) {
		this.bookCommentsService = bookCommentsService;
		this.deleteStrategyByFieldName = deleteStrategyByFieldName;
	}

	@Override
	public void onAfterDelete(final AfterDeleteEvent<Book> event) {
		super.onAfterDelete(event);
		final Set<Map.Entry<String, Object>> valueByFieldName = event.getSource().entrySet();

		for (Map.Entry<String, Object> valueByFieldNameEntry : valueByFieldName) {
			final var key = valueByFieldNameEntry.getKey();
			final var value = valueByFieldNameEntry.getValue();
			final var strategy = deleteStrategyByFieldName.get(key);

			if (Objects.isNull(strategy)) {
				throw new NotFoundStrategy("strategy for delete book comments not found!");
			}

			strategy.delete(value.toString());
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
