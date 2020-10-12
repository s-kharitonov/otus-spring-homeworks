package ru.otus.domain.dto;

import ru.otus.domain.BookComment;

public class BookCommentDto {
	private final Long id;
	private final String text;
	private final BookDto book;

	public BookCommentDto(final BookComment bookComment) {
		this.id = bookComment.getId();
		this.text = bookComment.getText();
		this.book = new BookDto.Builder()
				.id(bookComment.getBook().getId())
				.build();
	}

	private BookCommentDto(final Builder builder) {
		this.id = builder.id;
		this.text = builder.text;
		this.book = builder.book;
	}

	public Long getId() {
		return id;
	}

	public String getText() {
		return text;
	}

	public BookDto getBook() {
		return book;
	}

	@Override
	public String toString() {
		return "BookCommentDto{" +
				"id=" + id +
				", text='" + text + '\'' +
				", book=" + book +
				'}';
	}

	public static class Builder {
		private Long id;
		private String text;
		private BookDto book;

		public Builder id(final Long id) {
			this.id = id;
			return this;
		}

		public Builder text(final String text) {
			this.text = text;
			return this;
		}

		public Builder book(final BookDto book) {
			this.book = book;
			return this;
		}

		public BookCommentDto build() {
			return new BookCommentDto(this);
		}
	}
}
