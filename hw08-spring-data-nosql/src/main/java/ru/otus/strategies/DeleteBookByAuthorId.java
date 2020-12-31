package ru.otus.strategies;

import org.springframework.stereotype.Component;
import ru.otus.services.BooksService;

@Component
public class DeleteBookByAuthorId implements DeleteBookByAuthorStrategy{

	private final BooksService booksService;

	public DeleteBookByAuthorId(final BooksService booksService) {
		this.booksService = booksService;
	}

	@Override
	public void delete(final String id) {
		booksService.deleteByAuthorId(id);
	}

	@Override
	public String getFieldName() {
		return "_id";
	}
}
