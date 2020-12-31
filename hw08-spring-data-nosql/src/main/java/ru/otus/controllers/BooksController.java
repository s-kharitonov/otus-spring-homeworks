package ru.otus.controllers;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.domain.BookCandidate;
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
	public String create(@ShellOption(help = "enter book name") String name,
							 @ShellOption(help = "enter book publication date in format: dd.MM.yyyy") Date publicationDate,
							 @ShellOption(help = "enter book print length") int printLength,
							 @ShellOption(help = "enter exists author id") String authorId,
							 @ShellOption(help = "enter exists genre id") String genreId) {
		final var bookCandidate = new BookCandidate.Builder()
				.name(name)
				.printLength(printLength)
				.publicationDate(publicationDate)
				.authorId(authorId)
				.genreId(genreId)
				.build();

		return String.valueOf(booksFacade.save(bookCandidate));
	}

	@ShellMethod(value = "get book by id", group = "books", key = {"r-b", "read-book"})
	public String getBookById(@ShellOption(help = "enter book id") String id) {
		return String.valueOf(booksFacade.getById(id));
	}

	@ShellMethod(value = "get all books", group = "books", key = {"r-a-b", "read-all-books"})
	public String getAllBooks() {
		return booksFacade.getAll().stream()
				.map(String::valueOf)
				.collect(Collectors.joining(System.lineSeparator()));
	}

	@ShellMethod(value = "delete book by id", group = "books", key = { "d-b", "delete-book"})
	public void deleteBook(@ShellOption(help = "enter book id") String id) {
		booksFacade.deleteById(id);
	}

	@ShellMethod(value = "update book", group = "books", key = { "u-b", "update-book"})
	public String updateBook(@ShellOption(help = "enter book id") String bookId,
							 @ShellOption(help = "enter book name") String name,
							 @ShellOption(help = "enter book publication date in format: dd.MM.yyyy") Date publicationDate,
							 @ShellOption(help = "enter book print length") int printLength,
							 @ShellOption(help = "enter exists author id") String authorId,
							 @ShellOption(help = "enter exists genre id") String genreId) {
		final var bookCandidate = new BookCandidate.Builder()
				.bookId(bookId)
				.name(name)
				.printLength(printLength)
				.publicationDate(publicationDate)
				.authorId(authorId)
				.genreId(genreId)
				.build();

		return String.valueOf(booksFacade.save(bookCandidate));
	}
}
