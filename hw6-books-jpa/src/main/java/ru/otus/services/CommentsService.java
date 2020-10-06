package ru.otus.services;

import ru.otus.domain.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentsService {
	void save(Comment comment);

	Optional<Comment> getById(long id);

	List<Comment> getAll();

	boolean removeById(long id);
}
