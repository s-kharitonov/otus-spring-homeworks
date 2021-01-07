package ru.otus.strategies;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.repositories.BookCommentsRepository;

@Component
public class DeleteCommentByBookId implements DeleteCommentByBookStrategy {

	private final BookCommentsRepository commentsRepository;

	public DeleteCommentByBookId(final BookCommentsRepository commentsRepository) {
		this.commentsRepository = commentsRepository;
	}

	@Override
	@Transactional
	public void delete(final String id) {
		commentsRepository.deleteAllByBook_Id(id).subscribe();
	}

	@Override
	public String getFieldName() {
		return "_id";
	}
}
