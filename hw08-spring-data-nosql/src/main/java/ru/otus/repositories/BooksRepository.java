package ru.otus.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.domain.Book;

import java.util.List;

public interface BooksRepository extends MongoRepository<Book, String> {
	void deleteAllByAuthor_Id(String id);

	void deleteAllByGenre_Id(String id);

	List<Book> findAllByAuthor_Id(String id);

	List<Book> findAllByGenre_Id(String id);
}
