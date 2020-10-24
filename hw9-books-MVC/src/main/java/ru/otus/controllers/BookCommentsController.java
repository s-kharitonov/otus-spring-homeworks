package ru.otus.controllers;

import ru.otus.domain.dto.BookCommentDto;
import ru.otus.domain.dto.BookDto;
import ru.otus.services.facades.BookCommentsFacade;

import java.util.stream.Collectors;

public class BookCommentsController {

	private final BookCommentsFacade bookCommentsFacade;

	public BookCommentsController(final BookCommentsFacade bookCommentsFacade) {
		this.bookCommentsFacade = bookCommentsFacade;
	}

	public String create(String text, long bookId) {
		var book = new BookDto.Builder().id(bookId).build();
		var comment = new BookCommentDto.Builder()
				.book(book)
				.text(text)
				.build();

		return String.valueOf(bookCommentsFacade.save(comment));
	}

	public String update(long id, String text) {
		var comment = new BookCommentDto.Builder()
				.id(id)
				.text(text)
				.build();

		bookCommentsFacade.save(comment);

		return String.valueOf(bookCommentsFacade.getById(id));
	}

	public String getById(long id) {
		return String.valueOf(bookCommentsFacade.getById(id));
	}

	public String getAll() {
		return bookCommentsFacade.getAll().stream()
				.map(String::valueOf)
				.collect(Collectors.joining(System.lineSeparator()));
	}

	public void removeById(long id) {
		bookCommentsFacade.deleteById(id);
	}
}
