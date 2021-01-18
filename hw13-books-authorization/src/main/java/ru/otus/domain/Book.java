package ru.otus.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "BOOKS")
@NamedEntityGraphs({
		@NamedEntityGraph(name = "book.author", attributeNodes = @NamedAttributeNode("author")),
		@NamedEntityGraph(name = "book.genre", attributeNodes = @NamedAttributeNode("genre"))
})
public class Book {
	@Id
	@Column(name = "BOOK_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_s")
	@SequenceGenerator(name = "book_s", sequenceName = "BOOKS_S", allocationSize = 1)
	private Long id;

	@NotNull
	@NotEmpty
	@Size(max = 255)
	@Column(name = "NAME", nullable = false, unique = true, length = 255)
	private String name;

	@NotNull
	@Column(name = "PUBLICATION_DATE", nullable = false)
	@JsonFormat(pattern = Constants.DEFAULT_DATE_PATTERN)
	private Date publicationDate;

	@Min(value = 1)
	@Column(name = "PRINT_LENGTH", nullable = false)
	private int printLength;

	@NotNull
	@JoinColumn(name = "AUTHOR_ID", nullable = false)
	@ManyToOne(targetEntity = Author.class, fetch = FetchType.EAGER)
	private Author author;

	@NotNull
	@JoinColumn(name = "GENRE_ID", nullable = false)
	@ManyToOne(targetEntity = Genre.class, fetch = FetchType.EAGER)
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
