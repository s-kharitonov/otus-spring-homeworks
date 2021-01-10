package ru.otus.strategies;

import org.springframework.stereotype.Component;
import ru.otus.services.BookCommentsService;

@Component
public class DeleteCommentByBookId implements DeleteEntityStrategy {

	private final BookCommentsService bookCommentsService;

	public DeleteCommentByBookId(final BookCommentsService bookCommentsService) {
		this.bookCommentsService = bookCommentsService;
	}

	@Override
	public void delete(final String id) {
		bookCommentsService.deleteByBookId(id);
	}

	@Override
	public String getCollectionFieldName() {
		return "BOOKS._id";
	}
}
