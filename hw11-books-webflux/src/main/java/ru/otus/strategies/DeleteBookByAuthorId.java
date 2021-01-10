package ru.otus.strategies;

import org.springframework.stereotype.Component;
import ru.otus.repositories.BooksRepository;

@Component
public class DeleteBookByAuthorId implements DeleteEntityStrategy {

	private final BooksRepository booksService;

	public DeleteBookByAuthorId(final BooksRepository booksRepository) {
		this.booksService = booksRepository;
	}

	@Override
	public void delete(final String id) {
		booksService.deleteAllByAuthor_Id(id).subscribe();
	}

	@Override
	public String getCollectionFieldName() {
		return "AUTHORS._id";
	}
}
