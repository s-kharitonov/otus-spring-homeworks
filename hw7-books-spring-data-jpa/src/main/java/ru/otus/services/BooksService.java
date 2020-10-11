package ru.otus.services;

import ru.otus.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BooksService {
	void save(Book book);

	Optional<Book> getById(long id);

	List<Book> getAll();

	boolean removeById(long id);
}
