package ru.otus.strategies;

import org.springframework.stereotype.Component;
import ru.otus.services.BookCommentsService;

@Component
public class DeleteCommentByBookAuthorId implements DeleteEntityStrategy {

	private final BookCommentsService bookCommentsService;

	public DeleteCommentByBookAuthorId(final BookCommentsService bookCommentsService) {
		this.bookCommentsService = bookCommentsService;
	}

	@Override
	public void delete(final String id) {
		bookCommentsService.deleteByAuthorId(id);
	}

	@Override
	public String getCollectionFieldName() {
		return "BOOKS.author._id";
	}
}
