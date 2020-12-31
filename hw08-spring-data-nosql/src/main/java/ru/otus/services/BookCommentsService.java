package ru.otus.services;

import ru.otus.domain.BookComment;

import java.util.List;
import java.util.Optional;

public interface BookCommentsService {
	BookComment save(BookComment comment);

	List<BookComment> saveAll(List<BookComment> comments);

	Optional<BookComment> getById(String id);

	List<BookComment> getByBookId(String id);

	List<BookComment> getByBooksIds(List<String> ids);

	List<BookComment> getAll();

	void deleteById(String id);

	void deleteByBookId(String id);

	void deleteByGenreId(String id);

	void deleteByAuthorId(String id);
}
