package ru.otus.strategies;

import org.springframework.stereotype.Component;
import ru.otus.repositories.BookCommentsRepository;

@Component
public class DeleteCommentByBookAuthorId implements DeleteEntityStrategy {

	private final BookCommentsRepository commentsRepository;

	public DeleteCommentByBookAuthorId(final BookCommentsRepository commentsRepository) {
		this.commentsRepository = commentsRepository;
	}

	@Override
	public void delete(final String id) {
		commentsRepository.deleteAllByBook_Author_Id(id).subscribe();
	}

	@Override
	public String getCollectionFieldName() {
		return "BOOKS.author._id";
	}
}
