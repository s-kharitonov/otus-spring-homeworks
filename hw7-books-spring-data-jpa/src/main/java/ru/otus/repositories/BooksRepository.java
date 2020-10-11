package ru.otus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.domain.Book;

public interface BooksRepository extends JpaRepository<Book, Long> {

}
