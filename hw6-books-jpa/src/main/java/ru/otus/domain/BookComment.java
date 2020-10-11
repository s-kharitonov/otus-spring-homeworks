package ru.otus.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "BOOKS_COMMENTS")
public class BookComment {
	@Id
	@Column(name = "BOOK_COMMENT_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_comment_s")
	@SequenceGenerator(name = "book_comment_s", sequenceName = "BOOK_COMMENTS_S", allocationSize = 1)
	private Long id;

	@NotNull
	@JoinColumn(name = "BOOK_ID", nullable = false)
	@OneToOne(targetEntity = Book.class, cascade = CascadeType.ALL)
	private Book book;

	@NotNull
	@JoinColumn(name = "COMMENT_ID", nullable = false)
	@OneToOne(targetEntity = Comment.class, cascade = CascadeType.ALL)
	private Comment comment;

	public BookComment() {
	}

	public BookComment(final Book book, final Comment comment) {
		this.book = book;
		this.comment = comment;
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(final Book book) {
		this.book = book;
	}

	public Comment getComment() {
		return comment;
	}

	public void setComment(final Comment comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "BookComment{" +
				"id=" + id +
				", book=" + book +
				", comment=" + comment +
				'}';
	}
}
