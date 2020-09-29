package ru.otus.dao;

import ru.otus.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BooksDao {
	Optional<Long> saveBook(Book book);

	Optional<Book> findBookById(long id);

	List<Book> findAllBooks();

	boolean removeBook(long id);

	boolean updateBook(Book book);
}
