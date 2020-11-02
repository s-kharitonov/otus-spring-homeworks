package ru.otus.domain.dto;

import ru.otus.domain.Author;

public class AuthorDto {
	private final Long id;
	private final String name;
	private final String surname;

	public AuthorDto(final Author author) {
		this.id = author.getId();
		this.name = author.getName();
		this.surname = author.getSurname();
	}

	private AuthorDto(final Builder builder) {
		this.id = builder.id;
		this.name = builder.name;
		this.surname = builder.surname;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public String getFullName() {
		return name + " " + surname;
	}

	@Override
	public String toString() {
		return "AuthorDto{" +
				"id=" + id +
				", name='" + name + '\'' +
				", surname='" + surname + '\'' +
				'}';
	}

	public static class Builder {
		private Long id;
		private String name;
		private String surname;

		public Builder id(final long id) {
			this.id = id;
			return this;
		}

		public Builder name(final String name) {
			this.name = name;
			return this;
		}

		public Builder surname(final String surname) {
			this.surname = surname;
			return this;
		}

		public AuthorDto build() {
			return new AuthorDto(this);
		}
	}
}
