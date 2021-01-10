package ru.otus.strategies;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.repositories.BookCommentsRepository;

@Component
public class DeleteCommentByBookId implements DeleteEntityStrategy {

	private final BookCommentsRepository bookCommentsRepository;

	public DeleteCommentByBookId(final BookCommentsRepository bookCommentsRepository) {
		this.bookCommentsRepository = bookCommentsRepository;
	}

	@Override
	@Transactional
	public void delete(final String id) {
		bookCommentsRepository.deleteAllByBook_Id(id).subscribe();
	}

	@Override
	public String getCollectionFieldName() {
		return "BOOKS._id";
	}
}
