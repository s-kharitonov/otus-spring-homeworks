package ru.otus.strategies;

import org.springframework.stereotype.Component;
import ru.otus.services.BookCommentsService;

@Component
public class DeleteCommentByBookAuthorId implements DeleteCommentByBookStrategy {

	private final BookCommentsService bookCommentsService;

	public DeleteCommentByBookAuthorId(final BookCommentsService bookCommentsService) {
		this.bookCommentsService = bookCommentsService;
	}

	@Override
	public void delete(final String id) {
		bookCommentsService.deleteByAuthorId(id);
	}

	@Override
	public String getFieldName() {
		return "author._id";
	}
}
