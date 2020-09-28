package ru.otus.domain.dto;

import ru.otus.domain.Book;

import java.util.Date;

public class BookDto {
	private final Long id;
	private final String name;
	private final Date publicationDate;
	private final int printLength;
	private final AuthorDto author;
	private final GenreDto genre;

	public BookDto(final Book book) {
		this.id = book.getId();
		this.name = book.getName();
		this.publicationDate = book.getPublicationDate();
		this.printLength = book.getPrintLength();
		this.author = new AuthorDto(book.getAuthor());
		this.genre = new GenreDto(book.getGenre());
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Date getPublicationDate() {
		return publicationDate;
	}

	public int getPrintLength() {
		return printLength;
	}

	public AuthorDto getAuthor() {
		return author;
	}

	public GenreDto getGenre() {
		return genre;
	}

	@Override
	public String toString() {
		return "BookDto{" +
				"id=" + id +
				", name='" + name + '\'' +
				", publicationDate=" + publicationDate +
				", printLength=" + printLength +
				", author=" + author +
				", genre=" + genre +
				'}';
	}
}
