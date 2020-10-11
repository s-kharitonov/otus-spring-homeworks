package ru.otus.dao;

import ru.otus.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BooksDao {
	void save(Book book);

	Optional<Book> findById(long id);

	List<Book> findAll();

	boolean removeById(long id);
}
