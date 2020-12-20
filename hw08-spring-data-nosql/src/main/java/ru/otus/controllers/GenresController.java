package ru.otus.controllers;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.domain.Genre;
import ru.otus.services.GenresService;

import java.util.stream.Collectors;

@ShellComponent
public class GenresController {

	private final GenresService genresService;

	public GenresController(final GenresService genresService) {
		this.genresService = genresService;
	}

	@ShellMethod(value = "create genre", group = "genres", key = {"c-g", "create-genre"})
	public String create(@ShellOption(help = "enter genre name") String name) {
		final var genre = new Genre.Builder().name(name).build();
		return String.valueOf(genresService.save(genre));
	}

	@ShellMethod(value = "get genre by id", group = "genres", key = {"r-g", "read-genre"})
	public String getById(@ShellOption(help = "enter genre id") String id) {
		return String.valueOf(genresService.getById(id));
	}

	@ShellMethod(value = "get all genres", group = "genres", key = {"r-a-g", "read-all-genres"})
	public String getAll() {
		return genresService.getAll().stream()
				.map(Genre::toString)
				.collect(Collectors.joining(System.lineSeparator()));
	}

	@ShellMethod(value = "delete genre by id", group = "genres", key = { "d-g", "delete-genre"})
	public void delete(@ShellOption(help = "enter genre id") String id) {
		genresService.deleteById(id);
	}

	@ShellMethod(value = "update genre", group = "genres", key = { "u-g", "update-genre"})
	public String update(@ShellOption(help = "enter genre id") String id,
						 @ShellOption(help = "enter genre name") String name) {
		var genre = new Genre.Builder()
				.id(id)
				.name(name)
				.build();

		return String.valueOf(genresService.save(genre));
	}
}
