package ru.otus.domain;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Author {
	private Long id;
	@NotNull
	@NotEmpty
	@Size(max = 255)
	private String name;
	@NotNull
	@NotEmpty
	@Size(max = 255)
	private String surname;

	public Author(final String name, final String surname) {
		this.name = name;
		this.surname = surname;
	}

	public Author(final Long id, final String name, final String surname) {
		this.id = id;
		this.name = name;
		this.surname = surname;
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(final String surname) {
		this.surname = surname;
	}

	@Override
	public String toString() {
		return "Author{" +
				"id=" + id +
				", name='" + name + '\'' +
				", surname='" + surname + '\'' +
				'}';
	}
}
