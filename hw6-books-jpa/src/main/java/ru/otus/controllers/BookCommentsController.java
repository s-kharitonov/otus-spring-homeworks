package ru.otus.controllers;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.domain.BookComment;
import ru.otus.domain.Constants;
import ru.otus.services.BookCommentsService;
import ru.otus.services.LocalizationService;

import java.util.Objects;
import java.util.stream.Collectors;

@ShellComponent
public class BookCommentsController {

	private final BookCommentsService bookCommentsService;
	private final LocalizationService localizationService;

	public BookCommentsController(final BookCommentsService bookCommentsService,
								  final LocalizationService localizationService) {
		this.bookCommentsService = bookCommentsService;
		this.localizationService = localizationService;
	}

	@ShellMethod(value = "create comment", group = "comments", key = {"c-c", "create-comment"})
	public String create(@ShellOption(help = "enter comment") String text) {
		var comment = new BookComment.Builder().text(text).build();

		bookCommentsService.save(comment);

		if (Objects.nonNull(comment.getId())) {
			return String.valueOf(comment);
		} else {
			return localizationService.localizeMessage(Constants.COMMENT_UNSUCCESSFUL_CREATED_MSG_KEY, comment);
		}
	}

	@ShellMethod(value = "update comment", group = "comments", key = {"u-c", "update-comment"})
	public String update(@ShellOption(help = "enter comment id") long id,
						 @ShellOption(help = "enter comment") String text) {
		var comment = new BookComment.Builder()
				.id(id)
				.text(text)
				.build();

		bookCommentsService.save(comment);

		return String.valueOf(comment);
	}

	@ShellMethod(value = "get comment by id", group = "comments", key = {"r-c", "read-comment"})
	public String getById(@ShellOption(help = "enter comment id") long id) {
		return bookCommentsService.getById(id)
				.map(String::valueOf)
				.orElse(localizationService.localizeMessage(Constants.COMMENT_NOT_FOUND_MSG_KEY, id));
	}

	@ShellMethod(value = "get all comments", group = "comments", key = {"r-a-c", "read-all-comments"})
	public String getAll() {
		return bookCommentsService.getAll().stream()
				.map(BookComment::toString)
				.collect(Collectors.joining(System.lineSeparator()));
	}

	@ShellMethod(value = "remove comment by id", group = "comments", key = {"d-c", "delete-comment"})
	public String removeById(@ShellOption(help = "delete comment by id") long id) {
		if (bookCommentsService.removeById(id)) {
			return localizationService.localizeMessage(Constants.COMMENT_SUCCESSFUL_REMOVED_MSG_KEY, id);
		} else {
			return localizationService.localizeMessage(Constants.COMMENT_UNSUCCESSFUL_REMOVED_MSG_KEY, id);
		}
	}
}