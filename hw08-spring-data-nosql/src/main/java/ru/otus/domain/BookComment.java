package ru.otus.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Document(collection = "BOOKS_COMMENTS")
public class BookComment {
	@Id
	private String id;

	@NotNull
	@NotEmpty
	@Field(name = "text")
	private String text;

	@NotNull
	@Field(name = "book")
	private Book book;

	public BookComment() {
	}

	public BookComment(final Builder builder) {
		this.id = builder.id;
		this.text = builder.text;
		this.book = builder.book;
	}

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(final String text) {
		this.text = text;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(final Book book) {
		this.book = book;
	}

	@Override
	public String toString() {
		return "BookComment{" +
				"id='" + id + '\'' +
				", text='" + text + '\'' +
				", book=" + book +
				'}';
	}

	public static class Builder {
		private String id;
		private String text;
		private Book book;

		public Builder id(final String id) {
			this.id = id;
			return this;
		}

		public Builder text(final String text) {
			this.text = text;
			return this;
		}

		public Builder book(final Book book) {
			this.book = book;
			return this;
		}

		public BookComment build() {
			return new BookComment(this);
		}
	}
}
