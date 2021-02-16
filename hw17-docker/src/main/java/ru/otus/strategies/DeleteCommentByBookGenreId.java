package ru.otus.strategies;

import org.springframework.stereotype.Component;
import ru.otus.repositories.BookCommentsRepository;

@Component
public class DeleteCommentByBookGenreId implements DeleteEntityStrategy {

	private final BookCommentsRepository commentsRepository;

	public DeleteCommentByBookGenreId(final BookCommentsRepository commentsRepository) {
		this.commentsRepository = commentsRepository;
	}

	@Override
	public void delete(final String id) {
		commentsRepository.deleteAllByBook_Genre_Id(id).subscribe();
	}

	@Override
	public String getCollectionFieldName() {
		return "BOOKS.genre._id";
	}
}
