package ru.otus.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.domain.Author;

public interface AuthorsRepository extends MongoRepository<Author, String> {
}
