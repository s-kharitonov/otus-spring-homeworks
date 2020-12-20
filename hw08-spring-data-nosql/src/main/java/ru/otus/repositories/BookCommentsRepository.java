package ru.otus.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.domain.BookComment;

import java.util.List;

public interface BookCommentsRepository extends MongoRepository<BookComment, String> {
	void deleteAllByBook_Id(String id);

	void deleteAllByBook_Author_id(String id);

	void deleteAllByBook_Genre_id(String id);

	List<BookComment> findAllByBook_Id(String id);

	List<BookComment> findAllByBook_IdIn(List<String> ids);
}
