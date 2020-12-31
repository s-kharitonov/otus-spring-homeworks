package ru.otus.domain;

public class BookCommentCandidate {
	private final String commentId;
	private final String text;
	private final String bookId;

	private BookCommentCandidate(final Builder builder) {
		this.commentId = builder.commentId;
		this.text = builder.text;
		this.bookId = builder.bookId;
	}

	public String getCommentId() {
		return commentId;
	}

	public String getText() {
		return text;
	}

	public String getBookId() {
		return bookId;
	}

	@Override
	public String toString() {
		return "BookCommentCandidate{" +
				"commentId='" + commentId + '\'' +
				", text='" + text + '\'' +
				", bookId='" + bookId + '\'' +
				'}';
	}

	public static class Builder {
		private String commentId;
		private String text;
		private String bookId;

		public Builder commentId(String commentId) {
			this.commentId = commentId;
			return this;
		}

		public Builder text(String text) {
			this.text = text;
			return this;
		}

		public Builder bookId(String bookId) {
			this.bookId = bookId;
			return this;
		}

		public BookCommentCandidate build() {
			return new BookCommentCandidate(this);
		}
	}
}
