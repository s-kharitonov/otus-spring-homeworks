package ru.otus.services;

import ru.otus.domain.BookComment;

import java.util.List;
import java.util.Optional;

public interface CommentsService {
	void save(BookComment bookComment);

	Optional<BookComment> getById(long id);

	List<BookComment> getAll();

	boolean removeById(long id);
}
