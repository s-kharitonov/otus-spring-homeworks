package ru.otus.domain;

import java.util.Date;

public class Book {
	private Long id;
	private String name;
	private Date publicationDate;
	private int printLength;
	private Author author;
	private Genre genre;

	public Book(final long id, final String name, final Date publicationDate,
				final int printLength, final Author author, final Genre genre) {
		this.id = id;
		this.name = name;
		this.publicationDate = publicationDate;
		this.printLength = printLength;
		this.author = author;
		this.genre = genre;
	}

	public Book(final String name, final Date publicationDate, final int printLength,
				final Author author, final Genre genre) {
		this.name = name;
		this.publicationDate = publicationDate;
		this.printLength = printLength;
		this.author = author;
		this.genre = genre;
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
}
