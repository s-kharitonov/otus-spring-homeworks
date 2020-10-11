package ru.otus.services.facades;

import ru.otus.domain.dto.BookCommentDto;

import java.util.List;
import java.util.Optional;

public interface BookCommentsFacade {
	BookCommentDto save(BookCommentDto bookComment);

	Optional<BookCommentDto> getById(long id);

	List<BookCommentDto> getAll();

	void deleteById(long id);
}
