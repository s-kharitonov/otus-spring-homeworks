package ru.otus.domain;

public class User {
	private final String name;
	private final String surname;

	public User(final String name, final String surname) {
		this.name = name;
		this.surname = surname;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	@Override
	public String toString() {
		return "User{" +
				"name='" + name + '\'' +
				", surname='" + surname + '\'' +
				'}';
	}
}
