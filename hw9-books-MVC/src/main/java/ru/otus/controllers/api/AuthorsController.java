package ru.otus.controllers.api;

import ru.otus.domain.Author;
import ru.otus.services.AuthorsService;

import java.util.stream.Collectors;

public class AuthorsController {

	private final AuthorsService authorsService;

	public AuthorsController(final AuthorsService authorsService) {
		this.authorsService = authorsService;
	}

	public String create(String name, String surname) {
		final var author = new Author.Builder()
				.name(name)
				.surname(surname)
				.build();

		return String.valueOf(authorsService.save(author));
	}

	public String getById(long id) {
		return String.valueOf(authorsService.getById(id));
	}

	public String getAll() {
		return authorsService.getAll().stream()
				.map(Author::toString)
				.collect(Collectors.joining(System.lineSeparator()));
	}

	public void remove(long id) {
		authorsService.deleteById(id);
	}

	public String update(long id, String name, String surname) {
		final var author = new Author.Builder()
				.id(id)
				.name(name)
				.surname(surname)
				.build();

		return String.valueOf(authorsService.save(author));
	}
}
