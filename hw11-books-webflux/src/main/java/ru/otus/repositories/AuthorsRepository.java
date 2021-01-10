package ru.otus.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.otus.domain.Author;

public interface AuthorsRepository extends ReactiveMongoRepository<Author, String> {
}
