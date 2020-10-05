package ru.otus.domain;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "GENRES")
public class Genre {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genres_s")
	@SequenceGenerator(name = "genres_s", sequenceName = "GENRES_S", allocationSize = 1)
	@Column(name = "GENRE_ID")
	private Long id;
	@NotNull
	@NotEmpty
	@Size(max = 255)
	@Column(name = "NAME", nullable = false, unique = true, length = 255)
	private String name;

	public Genre() {
	}

	private Genre(final Builder builder) {
		this.id = builder.id;
		this.name = builder.name;
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

	@Override
	public String toString() {
		return "Genre{" +
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

		public Genre build() {
			return new Genre(this);
		}
	}
}
