package ru.otus.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import ru.otus.domain.Book;

import java.util.List;

public interface BooksRepository extends JpaRepository<Book, Long> {
	@NonNull
	@EntityGraph(value = "book", attributePaths = {"author", "genre"})
	List<Book> findAll();
}
