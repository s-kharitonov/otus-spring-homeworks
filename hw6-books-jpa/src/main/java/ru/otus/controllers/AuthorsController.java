package ru.otus.controllers;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.domain.Author;
import ru.otus.domain.Constants;
import ru.otus.services.AuthorsService;
import ru.otus.services.LocalizationService;

import java.util.Objects;
import java.util.stream.Collectors;

@ShellComponent
public class AuthorsController {

	private final AuthorsService authorsService;
	private final LocalizationService localizationService;

	public AuthorsController(final AuthorsService authorsService,
							 final LocalizationService localizationService) {
		this.authorsService = authorsService;
		this.localizationService = localizationService;
	}

	@ShellMethod(value = "create author and get id", group = "authors", key = {"c-a", "create-author"})
	public String createAuthor(@ShellOption(help = "enter author name") String name,
							   @ShellOption(help = "enter author surname") String surname) {
		final var author = new Author.Builder()
				.name(name)
				.surname(surname)
				.build();

		authorsService.saveAuthor(author);

		if (Objects.nonNull(author.getId())) {
			return String.valueOf(author);
		} else {
			return localizationService.localizeMessage(Constants.AUTHOR_UNSUCCESSFUL_CREATED_MSG_KEY, author);
		}
	}

	@ShellMethod(value = "get author by id", group = "authors", key = {"r-a", "read-author"})
	public String getAuthorById(@ShellOption(help = "enter author id") long id) {
		return authorsService.getAuthorById(id)
				.map(Author::toString)
				.orElse(localizationService.localizeMessage(Constants.AUTHOR_NOT_FOUND_MSG_KEY, id));
	}

	@ShellMethod(value = "get all authors", group = "authors", key = {"r-a-a", "read-all-authors"})
	public String getAllAuthors() {
		return authorsService.getAllAuthors().stream()
				.map(Author::toString)
				.collect(Collectors.joining(System.lineSeparator()));
	}

	@ShellMethod(value = "remove author by id", group = "authors", key = { "d-a", "delete-author"})
	public String removeAuthor(@ShellOption(help = "enter author id") long id) {
		if (authorsService.removeAuthor(id)) {
			return localizationService.localizeMessage(Constants.AUTHOR_SUCCESSFUL_REMOVED_MSG_KEY, id);
		} else {
			return localizationService.localizeMessage(Constants.AUTHOR_UNSUCCESSFUL_REMOVED_MSG_KEY, id);
		}
	}

	@ShellMethod(value = "update author", group = "authors", key = { "u-a", "update-author"})
	public String updateAuthor(@ShellOption(help = "enter author id") long id,
							   @ShellOption(help = "enter author name") String name,
							   @ShellOption(help = "enter author surname") String surname) {
		final var author = new Author.Builder()
				.id(id)
				.name(name)
				.surname(surname)
				.build();

		authorsService.saveAuthor(author);

		if (Objects.nonNull(author.getId())) {
			return String.valueOf(author);
		} else {
			return localizationService.localizeMessage(Constants.AUTHOR_UNSUCCESSFUL_UPDATED_MSG_KEY, id);
		}
	}
}
