package ru.otus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.domain.Genre;

public interface GenresRepository extends JpaRepository<Genre, Long> {

}
