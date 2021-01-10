package ru.otus.domain;

public class AuthorDto {
	private final String id;
	private final String name;
	private final String surname;

	public AuthorDto(final Author author) {
		this.id = author.getId();
		this.name = author.getName();
		this.surname = author.getSurname();
	}

	public String getId() {
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
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", surname='" + surname + '\'' +
				'}';
	}
}
