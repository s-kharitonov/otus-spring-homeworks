package ru.otus.strategies;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.repositories.BooksRepository;

@Component
public class DeleteBookByAuthorId implements DeleteBookByAuthorStrategy{

	private final BooksRepository booksRepository;

	public DeleteBookByAuthorId(final BooksRepository booksRepository) {
		this.booksRepository = booksRepository;
	}

	@Override
	@Transactional
	public void delete(final String id) {
		booksRepository.deleteAllByAuthor_Id(id).subscribe();
	}

	@Override
	public String getFieldName() {
		return "_id";
	}
}
