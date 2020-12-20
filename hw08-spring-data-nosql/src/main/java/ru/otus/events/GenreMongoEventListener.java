package ru.otus.events;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;
import ru.otus.domain.Genre;
import ru.otus.services.BooksService;

import java.util.Objects;

@Component
public class GenreMongoEventListener extends AbstractMongoEventListener<Genre> {

	private final BooksService booksService;

	public GenreMongoEventListener(final BooksService booksService) {
		this.booksService = booksService;
	}

	@Override
	public void onAfterDelete(final AfterDeleteEvent<Genre> event) {
		super.onAfterDelete(event);
		final var source = event.getSource();
		final var genreId = source.get("_id").toString();

		if (!Objects.isNull(genreId)) {
			booksService.deleteByGenreId(genreId);
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
