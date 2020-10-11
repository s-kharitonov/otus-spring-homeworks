package ru.otus.services.facades;

import ru.otus.domain.dto.BookCommentDto;

import java.util.List;
import java.util.Optional;

public interface BookCommentsFacade {
	Optional<Long> save(BookCommentDto bookComment);

	Optional<BookCommentDto> getById(long id);

	List<BookCommentDto> getAll();

	boolean removeById(long id);
}
