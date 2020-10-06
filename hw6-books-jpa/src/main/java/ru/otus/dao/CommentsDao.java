package ru.otus.dao;

import ru.otus.domain.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentsDao {
	void save(Comment comment);

	Optional<Comment> findById(long id);

	List<Comment> findAll();

	boolean removeById(long id);
}
