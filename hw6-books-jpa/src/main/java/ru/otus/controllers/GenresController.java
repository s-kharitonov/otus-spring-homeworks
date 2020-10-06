package ru.otus.controllers;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.domain.Constants;
import ru.otus.domain.Genre;
import ru.otus.services.GenresService;
import ru.otus.services.LocalizationService;

import java.util.Objects;
import java.util.stream.Collectors;

@ShellComponent
public class GenresController {

	private final GenresService genresService;
	private final LocalizationService localizationService;

	public GenresController(final GenresService genresService,
							final LocalizationService localizationService) {
		this.genresService = genresService;
		this.localizationService = localizationService;
	}

	@ShellMethod(value = "create genre", group = "genres", key = {"c-g", "create-genre"})
	public String createGenre(@ShellOption(help = "enter genre name") String name) {
		final var genre = new Genre.Builder().name(name).build();

		genresService.saveGenre(genre);

		if (Objects.nonNull(genre.getId())) {
			return String.valueOf(genre);
		} else {
			return localizationService.localizeMessage(Constants.GENRE_UNSUCCESSFUL_CREATED_MSG_KEY, genre);
		}
	}

	@ShellMethod(value = "get genre by id", group = "genres", key = {"r-g", "read-genre"})
	public String getGenreById(@ShellOption(help = "enter genre id") long id) {
		return genresService.getGenreById(id)
				.map(Genre::toString)
				.orElse(localizationService.localizeMessage(Constants.GENRE_NOT_FOUND_MSG_KEY, id));
	}

	@ShellMethod(value = "get all genres", group = "genres", key = {"r-a-g", "read-all-genres"})
	public String getAllGenres() {
		return genresService.getAllGenres().stream()
				.map(Genre::toString)
				.collect(Collectors.joining(System.lineSeparator()));
	}

	@ShellMethod(value = "remove genre by id", group = "genres", key = { "d-g", "delete-genre"})
	public String removeGenre(@ShellOption(help = "enter genre id") long id) {
		if (genresService.removeGenre(id)) {
			return localizationService.localizeMessage(Constants.GENRE_SUCCESSFUL_REMOVED_MSG_KEY, id);
		} else {
			return localizationService.localizeMessage(Constants.GENRE_UNSUCCESSFUL_REMOVED_MSG_KEY, id);
		}
	}

	@ShellMethod(value = "update genre", group = "genres", key = { "u-g", "update-genre"})
	public String updateGenre(@ShellOption(help = "enter genre id") long id,
							  @ShellOption(help = "enter genre name") String name) {
		var genre = new Genre.Builder()
				.id(id)
				.name(name)
				.build();

		genresService.saveGenre(genre);

		if (Objects.nonNull(genre.getId())) {
			return String.valueOf(genre);
		} else {
			return localizationService.localizeMessage(Constants.GENRE_UNSUCCESSFUL_UPDATED_MSG_KEY, id);
		}
	}
}
