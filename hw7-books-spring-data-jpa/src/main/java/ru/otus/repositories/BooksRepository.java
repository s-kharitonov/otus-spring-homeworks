package ru.otus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import ru.otus.domain.Book;

import java.util.List;

public interface BooksRepository extends JpaRepository<Book, Long> {
	@NonNull
	@Query(name = "book.find.all")
	List<Book> findAll();
}
