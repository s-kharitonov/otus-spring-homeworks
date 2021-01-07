package ru.otus.domain;

public class BookCommentCandidate {
	private String text;
	private String bookId;

	public String getText() {
		return text;
	}

	public void setText(final String text) {
		this.text = text;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(final String bookId) {
		this.bookId = bookId;
	}

	@Override
	public String toString() {
		return "BookCommentCandidate{" +
				"text='" + text + '\'' +
				", bookId='" + bookId + '\'' +
				'}';
	}
}
