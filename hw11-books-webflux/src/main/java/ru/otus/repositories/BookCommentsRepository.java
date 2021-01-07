package ru.otus.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.domain.BookComment;

import java.util.List;

public interface BookCommentsRepository extends ReactiveMongoRepository<BookComment, String> {
	Mono<Void> deleteAllByBook_Id(String id);

	Mono<Void> deleteAllByBook_Author_Id(String id);

	Mono<Void> deleteAllByBook_Genre_Id(String id);

	Flux<BookComment> findAllByBook_IdIn(List<String> ids);

	Flux<BookComment> findAllByBook_Id(String id);
}
