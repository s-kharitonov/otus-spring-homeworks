package ru.otus.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.otus.domain.Genre;

public interface GenresRepository extends ReactiveMongoRepository<Genre, String> {
}
