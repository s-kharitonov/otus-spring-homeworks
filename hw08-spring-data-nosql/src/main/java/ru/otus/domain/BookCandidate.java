package ru.otus.domain;

import java.util.Date;

public class BookCandidate {
	private final String bookId;
	private final String name;
	private final Date publicationDate;
	private final int printLength;
	private final String genreId;
	private final String authorId;

	private BookCandidate(final Builder builder) {
		this.bookId = builder.bookId;
		this.name = builder.name;
		this.publicationDate = builder.publicationDate;
		this.printLength = builder.printLength;
		this.genreId = builder.genreId;
		this.authorId = builder.authorId;
	}

	public String getBookId() {
		return bookId;
	}

	public String getName() {
		return name;
	}

	public Date getPublicationDate() {
		return publicationDate;
	}

	public int getPrintLength() {
		return printLength;
	}

	public String getGenreId() {
		return genreId;
	}

	public String getAuthorId() {
		return authorId;
	}

	@Override
	public String toString() {
		return "BookCandidate{" +
				"id='" + bookId + '\'' +
				", name='" + name + '\'' +
				", publicationDate=" + publicationDate +
				", printLength=" + printLength +
				", genreId='" + genreId + '\'' +
				", authorId='" + authorId + '\'' +
				'}';
	}

	public static class Builder {
		private String bookId;
		private String name;
		private Date publicationDate;
		private int printLength;
		private String genreId;
		private String authorId;

		public Builder bookId(final String bookId) {
			this.bookId = bookId;
			return this;
		}

		public Builder name(final String name) {
			this.name = name;
			return this;
		}

		public Builder publicationDate(final Date publicationDate) {
			this.publicationDate = publicationDate;
			return this;
		}

		public Builder printLength(final int printLength) {
			this.printLength = printLength;
			return this;
		}

		public Builder genreId(final String genreId) {
			this.genreId = genreId;
			return this;
		}

		public Builder authorId(final String authorId) {
			this.authorId = authorId;
			return this;
		}

		public BookCandidate build() {
			return new BookCandidate(this);
		}
	}
}
