package ru.otus.strategies;

import org.springframework.stereotype.Component;
import ru.otus.services.BookCommentsService;

@Component
public class DeleteCommentByBookGenreId implements DeleteEntityStrategy {

	private final BookCommentsService bookCommentsService;

	public DeleteCommentByBookGenreId(final BookCommentsService bookCommentsService) {
		this.bookCommentsService = bookCommentsService;
	}

	@Override
	public void delete(final String id) {
		bookCommentsService.deleteByGenreId(id);
	}

	@Override
	public String getCollectionFieldName() {
		return "BOOKS.genre._id";
	}
}
