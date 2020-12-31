package ru.otus.controllers;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.domain.BookCommentCandidate;
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
						 @ShellOption(help = "enter book id") String bookId) {
		final var commentCandidate = new BookCommentCandidate.Builder()
				.text(text)
				.bookId(bookId)
				.build();

		return String.valueOf(bookCommentsFacade.save(commentCandidate));
	}

	@ShellMethod(value = "update comment", group = "book comments", key = {"u-c", "update-comment"})
	public String update(@ShellOption(help = "enter comment id") String commentId,
						 @ShellOption(help = "enter comment") String text,
						 @ShellOption(help = "enter book id") String bookId) {
		final var commentCandidate = new BookCommentCandidate.Builder()
				.commentId(commentId)
				.text(text)
				.bookId(bookId)
				.build();

		return String.valueOf(bookCommentsFacade.save(commentCandidate));
	}

	@ShellMethod(value = "get comment by id", group = "book comments", key = {"r-c", "read-comment"})
	public String getById(@ShellOption(help = "enter comment id") String id) {
		return String.valueOf(bookCommentsFacade.getById(id));
	}

	@ShellMethod(value = "get all comments", group = "book comments", key = {"r-a-c", "read-all-comments"})
	public String getAll() {
		return bookCommentsFacade.getAll().stream()
				.map(String::valueOf)
				.collect(Collectors.joining(System.lineSeparator()));
	}

	@ShellMethod(value = "delete comment by id", group = "book comments", key = {"d-c", "delete-comment"})
	public void deleteById(@ShellOption(help = "delete comment by id") String id) {
		bookCommentsFacade.deleteById(id);
	}
}
