package ru.otus.domain.dto;

import ru.otus.domain.Genre;

public class GenreDto {
	private final Long id;
	private final String name;

	public GenreDto(final Genre genre) {
		this.id = genre.getId();
		this.name = genre.getName();
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "GenreDto{" +
				"id=" + id +
				", name='" + name + '\'' +
				'}';
	}
}
