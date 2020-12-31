package ru.otus.strategies;

import org.springframework.stereotype.Component;
import ru.otus.services.BooksService;

@Component
public class DeleteBookByGenreId implements DeleteBookByGenreStrategy{

	private final BooksService booksService;

	public DeleteBookByGenreId(final BooksService booksService) {
		this.booksService = booksService;
	}

	@Override
	public void delete(final String id) {
		booksService.deleteByGenreId(id);
	}

	@Override
	public String getFieldName() {
		return "_id";
	}
}
