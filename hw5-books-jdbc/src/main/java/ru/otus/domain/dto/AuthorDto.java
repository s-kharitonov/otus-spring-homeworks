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

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	@Override
	public String toString() {
		return "AuthorDto{" +
				"id=" + id +
				", name='" + name + '\'' +
				", surname='" + surname + '\'' +
				'}';
	}
}
