package ru.otus.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Document(collection = "AUTHORS")
public class Author {
	@Id
	private String id;

	@NotNull
	@NotEmpty
	@Field(name = "name")
	private String name;

	@NotNull
	@NotEmpty
	@Field(name = "surname")
	private String surname;

	public Author() {
	}

	public Author(final Builder builder) {
		this.id = builder.id;
		this.name = builder.name;
		this.surname = builder.surname;
	}

	public String getId() {
		return id;
	}

	public void setId(final String id) {
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
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", surname='" + surname + '\'' +
				'}';
	}

	public static class Builder{
		private String id;
		private String name;
		private String surname;

		public Builder id(final String id){
			this.id = id;
			return this;
		}

		public Builder name(final String name){
			this.name = name;
			return this;
		}

		public Builder surname(final String surname){
			this.surname = surname;
			return this;
		}

		public Author build() {
			return new Author(this);
		}
	}
}
