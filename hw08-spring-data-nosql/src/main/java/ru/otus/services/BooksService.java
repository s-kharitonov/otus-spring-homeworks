package ru.otus.services;

import ru.otus.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BooksService {
	Book save(Book book);

	List<Book> saveAll(List<Book> books);

	Optional<Book> getById(String id);

	List<Book> getByGenreId(String id);

	List<Book> getByAuthorId(String id);

	List<Book> getAll();

	void deleteById(String id);

	void deleteByAuthorId(String id);

	void deleteByGenreId(String id);
}
