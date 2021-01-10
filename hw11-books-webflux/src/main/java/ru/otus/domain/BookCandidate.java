package ru.otus.domain;

import java.util.Date;

public class BookCandidate {
	private String bookId;
	private String name;
	private Date publicationDate;
	private int printLength;
	private String genreId;
	private String authorId;

	public String getBookId() {
		return bookId;
	}

	public void setBookId(final String bookId) {
		this.bookId = bookId;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public Date getPublicationDate() {
		return publicationDate;
	}

	public void setPublicationDate(final Date publicationDate) {
		this.publicationDate = publicationDate;
	}

	public int getPrintLength() {
		return printLength;
	}

	public void setPrintLength(final int printLength) {
		this.printLength = printLength;
	}

	public String getGenreId() {
		return genreId;
	}

	public void setGenreId(final String genreId) {
		this.genreId = genreId;
	}

	public String getAuthorId() {
		return authorId;
	}

	public void setAuthorId(final String authorId) {
		this.authorId = authorId;
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
}
