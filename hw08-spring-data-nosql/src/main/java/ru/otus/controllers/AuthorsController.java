package ru.otus.controllers;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.domain.Author;
import ru.otus.services.AuthorsService;

import java.util.stream.Collectors;

@ShellComponent
public class AuthorsController {

	private final AuthorsService authorsService;

	public AuthorsController(final AuthorsService authorsService) {
		this.authorsService = authorsService;
	}

	@ShellMethod(value = "create author and get id", group = "authors", key = {"c-a", "create-author"})
	public String create(@ShellOption(help = "enter author name") String name,
						 @ShellOption(help = "enter author surname") String surname) {
		final var author = new Author.Builder()
				.name(name)
				.surname(surname)
				.build();

		return String.valueOf(authorsService.save(author));
	}

	@ShellMethod(value = "get author by id", group = "authors", key = {"r-a", "read-author"})
	public String getById(@ShellOption(help = "enter author id") String id) {
		return String.valueOf(authorsService.getById(id));
	}

	@ShellMethod(value = "get all authors", group = "authors", key = {"r-a-a", "read-all-authors"})
	public String getAll() {
		return authorsService.getAll().stream()
				.map(Author::toString)
				.collect(Collectors.joining(System.lineSeparator()));
	}

	@ShellMethod(value = "delete author by id", group = "authors", key = { "d-a", "delete-author"})
	public void delete(@ShellOption(help = "enter author id") String id) {
		authorsService.deleteById(id);
	}

	@ShellMethod(value = "update author", group = "authors", key = { "u-a", "update-author"})
	public String update(@ShellOption(help = "enter author id") String id,
						 @ShellOption(help = "enter author name") String name,
						 @ShellOption(help = "enter author surname") String surname) {
		final var author = new Author.Builder()
				.id(id)
				.name(name)
				.surname(surname)
				.build();

		return String.valueOf(authorsService.save(author));
	}
}
