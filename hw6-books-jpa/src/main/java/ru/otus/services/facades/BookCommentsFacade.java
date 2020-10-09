package ru.otus.services.facades;

import ru.otus.domain.Comment;

public interface BookCommentsFacade {
	void save(long bookId, Comment comment);
}
