package ru.otus.services;

import ru.otus.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BooksService {
	Optional<Long> createBook(Book book);

	Optional<Book> getBookById(long id);

	List<Book> getAllBooks();

	boolean removeBook(long id);

	boolean updateBook(Book book);
}
