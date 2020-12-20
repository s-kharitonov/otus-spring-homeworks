package ru.otus.services.facades;

import ru.otus.domain.BookComment;
import ru.otus.domain.BookCommentCandidate;

import java.util.List;
import java.util.Optional;

public interface BookCommentsFacade {
	BookComment save(BookCommentCandidate commentCandidate);

	Optional<BookComment> getById(String id);

	List<BookComment> getAll();

	void deleteById(String id);
}
