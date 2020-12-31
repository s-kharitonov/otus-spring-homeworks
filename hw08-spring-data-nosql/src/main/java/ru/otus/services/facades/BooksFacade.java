package ru.otus.services.facades;

import ru.otus.domain.Book;
import ru.otus.domain.BookCandidate;

import java.util.List;
import java.util.Optional;

public interface BooksFacade {
	Book save(BookCandidate bookCandidate);

	Optional<Book> getById(String id);

	List<Book> getAll();

	void deleteById(String id);
}
