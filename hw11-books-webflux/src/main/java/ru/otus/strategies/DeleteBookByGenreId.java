package ru.otus.strategies;

import org.springframework.stereotype.Component;
import ru.otus.repositories.BooksRepository;

@Component
public class DeleteBookByGenreId implements DeleteEntityStrategy {

	private final BooksRepository booksRepository;

	public DeleteBookByGenreId(final BooksRepository booksRepository) {
		this.booksRepository = booksRepository;
	}

	@Override
	public void delete(final String id) {
		booksRepository.deleteAllByGenre_Id(id).subscribe();
	}

	@Override
	public String getCollectionFieldName() {
		return "GENRES._id";
	}
}
