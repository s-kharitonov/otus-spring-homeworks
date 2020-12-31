package ru.otus.strategies;

import org.springframework.stereotype.Component;
import ru.otus.services.BookCommentsService;

@Component
public class DeleteCommentByBookGenreId implements DeleteCommentByBookStrategy {

	private final BookCommentsService bookCommentsService;

	public DeleteCommentByBookGenreId(final BookCommentsService bookCommentsService) {
		this.bookCommentsService = bookCommentsService;
	}

	@Override
	public void delete(final String id) {
		bookCommentsService.deleteByGenreId(id);
	}

	@Override
	public String getFieldName() {
		return "genre._id";
	}
}
