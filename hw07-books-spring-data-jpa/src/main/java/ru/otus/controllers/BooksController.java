package ru.otus.controllers;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.domain.dto.AuthorDto;
import ru.otus.domain.dto.BookDto;
import ru.otus.domain.dto.GenreDto;
import ru.otus.services.facades.BooksFacade;

import java.util.Date;
import java.util.stream.Collectors;

@ShellComponent
public class BooksController {

	private final BooksFacade booksFacade;

	public BooksController(final BooksFacade booksFacade) {
		this.booksFacade = booksFacade;
	}

	@ShellMethod(value = "create book and get id", group = "books", key = {"c-b", "create-book"})
	public String createBook(@ShellOption(help = "enter book name") String name,
							 @ShellOption(help = "enter book publication date in format: dd.MM.yyyy") Date publicationDate,
							 @ShellOption(help = "enter book print length") int printLength,
							 @ShellOption(help = "enter exists author id") long authorId,
							 @ShellOption(help = "enter exists genre id") long genreId) {
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

	@ShellMethod(value = "get book by id", group = "books", key = {"r-b", "read-book"})
	public String getBookById(@ShellOption(help = "enter book id") long id) {
		return String.valueOf(booksFacade.getById(id));
	}

	@ShellMethod(value = "get all books", group = "books", key = {"r-a-b", "read-all-books"})
	public String getAllBooks() {
		return booksFacade.getAll().stream()
				.map(String::valueOf)
				.collect(Collectors.joining(System.lineSeparator()));
	}

	@ShellMethod(value = "remove book by id", group = "books", key = { "d-b", "delete-book"})
	public void removeBook(@ShellOption(help = "enter book id") long id) {
		booksFacade.deleteById(id);
	}

	@ShellMethod(value = "update book", group = "books", key = { "u-b", "update-book"})
	public String updateBook(@ShellOption(help = "enter book id") long bookId,
							 @ShellOption(help = "enter book name") String name,
							 @ShellOption(help = "enter book publication date in format: dd.MM.yyyy") Date publicationDate,
							 @ShellOption(help = "enter book print length") int printLength,
							 @ShellOption(help = "enter exists author id") long authorId,
							 @ShellOption(help = "enter exists genre id") long genreId) {
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
