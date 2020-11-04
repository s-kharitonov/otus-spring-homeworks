package ru.otus.domain.dto;

import ru.otus.domain.Genre;

public class GenreDto {
	private final Long id;
	private final String name;

	public GenreDto(final Genre genre) {
		this.id = genre.getId();
		this.name = genre.getName();
	}

	private GenreDto(final Builder builder) {
		this.id = builder.id;
		this.name = builder.name;
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

	public static class Builder {
		private Long id;
		private String name;

		public Builder id(final Long id) {
			this.id = id;
			return this;
		}

		public Builder name(final String name) {
			this.name = name;
			return this;
		}

		public GenreDto build() {
			return new GenreDto(this);
		}
	}
}
