package ru.otus.strategies;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.repositories.BookCommentsRepository;

@Component
public class DeleteCommentByBookGenreId implements DeleteCommentByBookStrategy {

	private final BookCommentsRepository commentsRepository;

	public DeleteCommentByBookGenreId(final BookCommentsRepository commentsRepository) {
		this.commentsRepository = commentsRepository;
	}

	@Override
	@Transactional
	public void delete(final String id) {
		commentsRepository.deleteAllByBook_Genre_Id(id).subscribe();
	}

	@Override
	public String getFieldName() {
		return "genre._id";
	}
}
