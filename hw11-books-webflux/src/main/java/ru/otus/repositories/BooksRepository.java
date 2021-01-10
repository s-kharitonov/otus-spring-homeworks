package ru.otus.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.domain.Book;

public interface BooksRepository extends ReactiveMongoRepository<Book, String> {
	Flux<Book> findAllByGenre_Id(String id);

	Flux<Book> findAllByAuthor_Id(String id);

	Mono<Void> deleteAllByAuthor_Id(String id);

	Mono<Void> deleteAllByGenre_Id(String id);
}
