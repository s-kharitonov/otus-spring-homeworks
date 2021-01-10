package ru.otus.events;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;
import ru.otus.commands.DeleteEntityCommand;
import ru.otus.domain.Book;
import ru.otus.repositories.BookCommentsRepository;

import javax.annotation.Nonnull;

@Component
public class BookMongoEventListener extends AbstractMongoEventListener<Book> {

	private final BookCommentsRepository commentsRepository;
	private final DeleteEntityCommand deleteEntityCommand;

	public BookMongoEventListener(final BookCommentsRepository commentsRepository,
								  final DeleteEntityCommand deleteEntityCommand) {
		this.commentsRepository = commentsRepository;
		this.deleteEntityCommand = deleteEntityCommand;
	}

	@Override
	public void onAfterDelete(@Nonnull final AfterDeleteEvent<Book> event) {
		super.onAfterDelete(event);
		final var collectionName = event.getCollectionName();
		final var source = event.getSource();

		deleteEntityCommand.delete(collectionName, source);
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
