package ru.otus.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Document(collection = "GENRES")
public class Genre {
	@Id
	private String id;

	@NotNull
	@NotEmpty
	@Field(name = "name")
	private String name;

	public Genre() {
	}

	public Genre(final Builder builder) {
		this.id = builder.id;
		this.name = builder.name;
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

	@Override
	public String toString() {
		return "Genre{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				'}';
	}

	public static class Builder {
		private String id;
		private String name;

		public Builder id(final String id) {
			this.id = id;
			return this;
		}

		public Builder name(final String name) {
			this.name = name;
			return this;
		}

		public Genre build() {
			return new Genre(this);
		}
	}
}
