package ru.otus.domain;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "AUTHORS")
public class Author {
	@Id
	@Column(name = "AUTHOR_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "author_s")
	@SequenceGenerator(name = "author_s", sequenceName = "AUTHORS_S", allocationSize = 1)
	private Long id;

	@NotNull
	@NotEmpty
	@Size(max = 255)
	@Column(name = "NAME", nullable = false, length = 255)
	private String name;

	@NotNull
	@NotEmpty
	@Size(max = 255)
	@Column(name = "SURNAME", nullable = false, length = 255)
	private String surname;

	public Author() {
	}

	private Author(final Builder builder) {
		this.id = builder.id;
		this.name = builder.name;
		this.surname = builder.surname;
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

	public static class Builder{
		private Long id;
		private String name;
		private String surname;

		public Builder id(final Long id){
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
