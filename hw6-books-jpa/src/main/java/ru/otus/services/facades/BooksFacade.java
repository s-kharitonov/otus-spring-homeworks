package ru.otus.services.facades;

import ru.otus.domain.dto.BookDto;

import java.util.List;
import java.util.Optional;

public interface BooksFacade {
	Optional<Long> save(BookDto book);

	Optional<BookDto> getById(long id);

	List<BookDto> getAll();

	boolean removeById(long id);
}
