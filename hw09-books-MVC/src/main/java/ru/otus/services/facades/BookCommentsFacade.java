package ru.otus.services.facades;

import ru.otus.domain.BookComment;
import ru.otus.domain.BookCommentCandidate;
import ru.otus.domain.dto.BookCommentDto;

import java.util.List;
import java.util.Optional;

public interface BookCommentsFacade {
	BookCommentDto create(BookCommentCandidate candidate);

	Optional<BookCommentDto> getById(long id);

	List<BookCommentDto> getAll();

	void deleteById(long id);

	void update(BookComment bookComment);
}
