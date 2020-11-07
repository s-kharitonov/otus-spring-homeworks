package ru.otus.services.facades;

import ru.otus.domain.Book;
import ru.otus.domain.BookCandidate;
import ru.otus.domain.dto.BookDto;

import java.util.List;
import java.util.Optional;

public interface BooksFacade {
	BookDto create(BookCandidate candidate);

	Optional<BookDto> getById(long id);

	List<BookDto> getAll();

	void deleteById(long id);

	void update(Book book);
}
