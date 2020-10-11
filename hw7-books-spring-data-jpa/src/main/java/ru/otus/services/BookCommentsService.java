package ru.otus.services;

import ru.otus.domain.BookComment;

import java.util.List;
import java.util.Optional;

public interface BookCommentsService {
	BookComment save(BookComment bookComment);

	Optional<BookComment> getById(long id);

	List<BookComment> getAll();

	void deleteById(long id);
}
