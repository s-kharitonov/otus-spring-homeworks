package ru.otus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.domain.BookComment;

public interface BookCommentsRepository extends JpaRepository<BookComment, Long> {

}
