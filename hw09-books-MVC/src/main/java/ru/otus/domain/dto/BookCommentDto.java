package ru.otus.domain.dto;

import ru.otus.domain.BookComment;

public class BookCommentDto {
	private final Long id;
	private final String text;

	public BookCommentDto(final BookComment bookComment) {
		this.id = bookComment.getId();
		this.text = bookComment.getText();
	}

	private BookCommentDto(final Builder builder) {
		this.id = builder.id;
		this.text = builder.text;
	}

	public Long getId() {
		return id;
	}

	public String getText() {
		return text;
	}

	@Override
	public String toString() {
		return "BookCommentDto{" +
				"id=" + id +
				", text='" + text + '\'' +
				'}';
	}

	public static class Builder {
		private Long id;
		private String text;

		public Builder id(final Long id) {
			this.id = id;
			return this;
		}

		public Builder text(final String text) {
			this.text = text;
			return this;
		}

		public BookCommentDto build() {
			return new BookCommentDto(this);
		}
	}
}
