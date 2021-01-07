package ru.otus.strategies;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.repositories.BookCommentsRepository;

@Component
public class DeleteCommentByBookAuthorId implements DeleteCommentByBookStrategy {

	private final BookCommentsRepository commentsRepository;

	public DeleteCommentByBookAuthorId(final BookCommentsRepository commentsRepository) {
		this.commentsRepository = commentsRepository;
	}

	@Override
	@Transactional
	public void delete(final String id) {
		commentsRepository.deleteAllByBook_Author_Id(id).subscribe();
	}

	@Override
	public String getFieldName() {
		return "author._id";
	}
}
