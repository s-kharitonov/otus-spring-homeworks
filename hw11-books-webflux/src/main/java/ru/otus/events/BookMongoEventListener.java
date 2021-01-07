package ru.otus.events;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;
import ru.otus.domain.Book;
import ru.otus.exceptions.NotFoundStrategy;
import ru.otus.repositories.BookCommentsRepository;
import ru.otus.strategies.DeleteCommentByBookStrategy;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Component
public class BookMongoEventListener extends AbstractMongoEventListener<Book> {

	private final BookCommentsRepository commentsRepository;
	private final Map<String, DeleteCommentByBookStrategy> deleteStrategyByFieldName;

	public BookMongoEventListener(final BookCommentsRepository commentsRepository,
								  @Qualifier("deleteCommentByBookStrategy")
								  final Map<String, DeleteCommentByBookStrategy> deleteStrategyByFieldName) {
		this.commentsRepository = commentsRepository;
		this.deleteStrategyByFieldName = deleteStrategyByFieldName;
	}

	@Override
	public void onAfterDelete(@Nonnull final AfterDeleteEvent<Book> event) {
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
	public void onAfterSave(@Nonnull final AfterSaveEvent<Book> event) {
		super.onAfterSave(event);
		final var book = event.getSource();
		final var bookId = book.getId();

		commentsRepository.findAllByBook_Id(bookId)
				.doOnNext(comment -> comment.setBook(book))
				.collectList()
				.doOnNext(commentsRepository::saveAll)
				.subscribe();
	}
}
