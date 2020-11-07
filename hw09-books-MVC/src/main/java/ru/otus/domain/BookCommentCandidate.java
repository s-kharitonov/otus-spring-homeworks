package ru.otus.domain;

public class BookCommentCandidate {
	private String text;
	private long bookId;

	public String getText() {
		return text;
	}

	public void setText(final String text) {
		this.text = text;
	}

	public long getBookId() {
		return bookId;
	}

	public void setBookId(final long bookId) {
		this.bookId = bookId;
	}

	@Override
	public String toString() {
		return "BookCommentCandidate{" +
				"text='" + text + '\'' +
				", bookId=" + bookId +
				'}';
	}
}
