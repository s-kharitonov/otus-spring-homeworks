package ru.otus.dao;

import ru.otus.domain.BookComment;

import java.util.List;
import java.util.Optional;

public interface CommentsDao {
	void save(BookComment bookComment);

	Optional<BookComment> findById(long id);

	List<BookComment> findAll();

	boolean removeById(long id);
}
