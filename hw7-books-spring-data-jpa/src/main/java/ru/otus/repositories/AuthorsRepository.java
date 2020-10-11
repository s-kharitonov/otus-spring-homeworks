package ru.otus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.domain.Author;

public interface AuthorsRepository extends JpaRepository<Author, Long> {

}
