package ru.otus.domain;

import java.util.Date;

public class BookCandidate {
	private String name;
	private Date publicationDate;
	private int printLength;
	private long authorId;
	private long genreId;

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

	public long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(final long authorId) {
		this.authorId = authorId;
	}

	public long getGenreId() {
		return genreId;
	}

	public void setGenreId(final long genreId) {
		this.genreId = genreId;
	}

	@Override
	public String toString() {
		return "BookCandidate{" +
				"name='" + name + '\'' +
				", publicationDate=" + publicationDate +
				", printLength=" + printLength +
				", authorId=" + authorId +
				", genreId=" + genreId +
				'}';
	}
}
