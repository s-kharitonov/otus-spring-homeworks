package ru.otus.controllers;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.domain.dto.BookCommentDto;
import ru.otus.domain.dto.BookDto;
import ru.otus.services.facades.BookCommentsFacade;

import java.util.stream.Collectors;

@ShellComponent
public class BookCommentsController {

	private final BookCommentsFacade bookCommentsFacade;

	public BookCommentsController(final BookCommentsFacade bookCommentsFacade) {
		this.bookCommentsFacade = bookCommentsFacade;
	}

	@ShellMethod(value = "create comment and get id", group = "book comments", key = {"c-c", "create-comment"})
	public String create(@ShellOption(help = "enter comment") String text,
						 @ShellOption(help = "enter book id") long bookId) {
		var book = new BookDto.Builder().id(bookId).build();
		var comment = new BookCommentDto.Builder()
				.book(book)
				.text(text)
				.build();

		return String.valueOf(bookCommentsFacade.save(comment));
	}

	@ShellMethod(value = "update comment", group = "book comments", key = {"u-c", "update-comment"})
	public String update(@ShellOption(help = "enter comment id") long id,
						 @ShellOption(help = "enter comment") String text) {
		var comment = new BookCommentDto.Builder()
				.id(id)
				.text(text)
				.build();

		bookCommentsFacade.save(comment);

		return String.valueOf(bookCommentsFacade.getById(id));
	}

	@ShellMethod(value = "get comment by id", group = "book comments", key = {"r-c", "read-comment"})
	public String getById(@ShellOption(help = "enter comment id") long id) {
		return String.valueOf(bookCommentsFacade.getById(id));
	}

	@ShellMethod(value = "get all comments", group = "book comments", key = {"r-a-c", "read-all-comments"})
	public String getAll() {
		return bookCommentsFacade.getAll().stream()
				.map(String::valueOf)
				.collect(Collectors.joining(System.lineSeparator()));
	}

	@ShellMethod(value = "remove comment by id", group = "book comments", key = {"d-c", "delete-comment"})
	public void removeById(@ShellOption(help = "delete comment by id") long id) {
		bookCommentsFacade.deleteById(id);
	}
}
