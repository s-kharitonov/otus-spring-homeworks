package ru.otus.controllers.api;

import ru.otus.domain.dto.AuthorDto;
import ru.otus.domain.dto.BookDto;
import ru.otus.domain.dto.GenreDto;
import ru.otus.services.facades.BooksFacade;

import java.util.Date;
import java.util.stream.Collectors;

public class BooksController {

	private final BooksFacade booksFacade;

	public BooksController(final BooksFacade booksFacade) {
		this.booksFacade = booksFacade;
	}

	public String createBook(String name, Date publicationDate, int printLength, long authorId, long genreId) {
		final var author = new AuthorDto.Builder().id(authorId).build();
		final var genre = new GenreDto.Builder().id(genreId).build();
		final var book = new BookDto.Builder()
				.name(name)
				.publicationDate(publicationDate)
				.printLength(printLength)
				.author(author)
				.genre(genre)
				.build();

		return String.valueOf(booksFacade.save(book));
	}

	public String getBookById(long id) {
		return String.valueOf(booksFacade.getById(id));
	}

	public String getAllBooks() {
		return booksFacade.getAll().stream()
				.map(String::valueOf)
				.collect(Collectors.joining(System.lineSeparator()));
	}

	public void removeBook(long id) {
		booksFacade.deleteById(id);
	}

	public String updateBook(long bookId, String name, Date publicationDate, int printLength, long authorId, long genreId) {
		final var author = new AuthorDto.Builder().id(authorId).build();
		final var genre = new GenreDto.Builder().id(genreId).build();
		final var book = new BookDto.Builder()
				.id(bookId)
				.name(name)
				.publicationDate(publicationDate)
				.printLength(printLength)
				.author(author)
				.genre(genre)
				.build();

		return String.valueOf(booksFacade.save(book));
	}
}
