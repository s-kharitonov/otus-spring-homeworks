package ru.otus.domain;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

public class Book {
	private Long id;
	@NotNull
	@NotEmpty
	@Size(max = 255)
	private String name;
	@NotNull
	private Date publicationDate;
	@Min(value = 1)
	private int printLength;
	@NotNull
	private Author author;
	@NotNull
	private Genre genre;

	public Book() {
	}

	private Book(final Builder builder) {
		this.id = builder.id;
		this.name = builder.name;
		this.publicationDate = builder.publicationDate;
		this.printLength = builder.printLength;
		this.author = builder.author;
		this.genre = builder.genre;
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

	public Date getPublicationDate() {
		return publicationDate;
	}

	public void setPublicationDate(final Date publicationDate) {
		this.publicationDate = publicationDate;
	}

	public int getPrintLength() {
		return printLength;
	}

	public void setPrintLength(final int printLength) {
		this.printLength = printLength;
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(final Author author) {
		this.author = author;
	}

	public Genre getGenre() {
		return genre;
	}

	public void setGenre(final Genre genre) {
		this.genre = genre;
	}

	@Override
	public String toString() {
		return "Book{" +
				"id=" + id +
				", name='" + name + '\'' +
				", publicationDate=" + publicationDate +
				", printLength=" + printLength +
				", author=" + author +
				", genre=" + genre +
				'}';
	}

	public static class Builder {
		private Long id;
		private String name;
		private Date publicationDate;
		private int printLength;
		private Author author;
		private Genre genre;

		public Builder id(final Long id) {
			this.id = id;
			return this;
		}

		public Builder name(final String name) {
			this.name = name;
			return this;
		}

		public Builder publicationDate(final Date publicationDate) {
			this.publicationDate = publicationDate;
			return this;
		}

		public Builder printLength(final int printLength) {
			this.printLength = printLength;
			return this;
		}

		public Builder author(final Author author) {
			this.author = author;
			return this;
		}

		public Builder genre(final Genre genre) {
			this.genre = genre;
			return this;
		}

		public Book build() {
			return new Book(this);
		}
	}
}
