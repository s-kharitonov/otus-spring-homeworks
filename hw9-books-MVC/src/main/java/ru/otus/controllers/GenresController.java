package ru.otus.controllers;

import ru.otus.domain.Genre;
import ru.otus.services.GenresService;

import java.util.stream.Collectors;

public class GenresController {

	private final GenresService genresService;

	public GenresController(final GenresService genresService) {
		this.genresService = genresService;
	}

	public String create(String name) {
		final var genre = new Genre.Builder().name(name).build();
		return String.valueOf(genresService.save(genre));
	}

	public String getById(long id) {
		return String.valueOf(genresService.getById(id));
	}

	public String getAll() {
		return genresService.getAll().stream()
				.map(Genre::toString)
				.collect(Collectors.joining(System.lineSeparator()));
	}

	public void remove(long id) {
		genresService.deleteById(id);
	}

	public String update(long id, String name) {
		var genre = new Genre.Builder()
				.id(id)
				.name(name)
				.build();

		return String.valueOf(genresService.save(genre));
	}
}
