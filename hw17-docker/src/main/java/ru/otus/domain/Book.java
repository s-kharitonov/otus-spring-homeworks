package ru.otus.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Document(collection = "BOOKS")
public class Book {
	@Id
	private String id;

	@NotBlank
	@Field(name = "name")
	private String name;

	@NotNull
	@Field(name = "publicationDate")
	@JsonFormat(pattern = Constants.DEFAULT_DATE_PATTERN)
	private Date publicationDate;

	@Min(value = 1)
	@Field(name = "printLength")
	private int printLength;

	@NotNull
	@Field(name = "author")
	private Author author;

	@NotNull
	@Field(name = "genre")
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
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", publicationDate=" + publicationDate +
				", printLength=" + printLength +
				", author=" + author +
				", genre=" + genre +
				'}';
	}

	public static class Builder {
		private String id;
		private String name;
		private Date publicationDate;
		private int printLength;
		private Author author;
		private Genre genre;

		public Builder id(final String id) {
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
