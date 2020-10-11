package ru.otus.domain;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "BOOKS_COMMENTS")
public class BookComment {
	@Id
	@Column(name = "COMMENT_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_comment_s")
	@SequenceGenerator(name = "book_comment_s", sequenceName = "BOOK_COMMENTS_S", allocationSize = 1)
	private Long id;

	@NotNull
	@NotEmpty
	@Size(max = 255)
	@Column(name = "TEXT", nullable = false, length = 255)
	private String text;

	@NotNull
	@JoinColumn(name = "BOOK_ID", nullable = false)
	@ManyToOne(targetEntity = Book.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Book book;

	public BookComment() {
	}

	private BookComment(final Builder builder) {
		this.id = builder.id;
		this.text = builder.text;
		this.book = builder.book;
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
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
		return "Comment{" +
				"id=" + id +
				", text='" + text + '\'' +
				", book=" + book +
				'}';
	}

	public static class Builder {
		private Long id;
		private String text;
		private Book book;

		public Builder id(long id) {
			this.id = id;
			return this;
		}

		public Builder text(String text) {
			this.text = text;
			return this;
		}

		public Builder book(Book book) {
			this.book = book;
			return this;
		}

		public BookComment build() {
			return new BookComment(this);
		}
	}
}
