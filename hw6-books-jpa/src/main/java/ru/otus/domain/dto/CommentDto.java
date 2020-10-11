package ru.otus.domain.dto;

import ru.otus.domain.BookComment;

public class CommentDto {
	private final long id;
	private final String text;

	public CommentDto(final BookComment bookComment) {
		this.id = bookComment.getId();
		this.text = bookComment.getText();
	}

	public long getId() {
		return id;
	}

	public String getText() {
		return text;
	}

	@Override
	public String toString() {
		return "CommentDto{" +
				"id=" + id +
				", text='" + text + '\'' +
				'}';
	}
}
