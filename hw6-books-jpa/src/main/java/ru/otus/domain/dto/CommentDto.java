package ru.otus.domain.dto;

import ru.otus.domain.Comment;

public class CommentDto {
	private final long id;
	private final String text;

	public CommentDto(final Comment comment) {
		this.id = comment.getId();
		this.text = comment.getText();
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
