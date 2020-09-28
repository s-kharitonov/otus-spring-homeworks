package ru.otus.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.domain.Author;
import ru.otus.domain.Book;
import ru.otus.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class BooksDaoJdbc implements BooksDao {

	private static final Logger logger = LoggerFactory.getLogger(BooksDaoJdbc.class);

	private final NamedParameterJdbcOperations jdbcOperations;

	public BooksDaoJdbc(final NamedParameterJdbcOperations jdbcOperations) {
		this.jdbcOperations = jdbcOperations;
	}

	@Override
	public Optional<Long> saveBook(final Book book) {
		final var keyHolder = new GeneratedKeyHolder();
		final var params = new MapSqlParameterSource();

		params.addValue("authorId", book.getAuthor().getId());
		params.addValue("genreId", book.getGenre().getId());
		params.addValue("name", book.getName());
		params.addValue("publicationDate", book.getPublicationDate());
		params.addValue("printLength", book.getPrintLength());

		try {
			jdbcOperations.update(
					"INSERT INTO BOOKS (AUTHOR_ID, GENRE_ID, NAME, PUBLICATION_DATE, PRINT_LENGTH) " +
							"VALUES (:authorId, :genreId, :name, :publicationDate, :printLength)",
					params,
					keyHolder
			);

			return Optional.ofNullable((Long) keyHolder.getKey());
		} catch (DataAccessException e) {
			logger.error("error creating book: {}", book, e);
			return Optional.empty();
		}
	}

	@Override
	public Optional<Book> findBookById(final long id) {
		try {
			final Book book = jdbcOperations.queryForObject(
					"SELECT B.BOOK_ID," +
							"      B.NAME AS BOOK_NAME," +
							"      B.PUBLICATION_DATE," +
							"      B.PRINT_LENGTH," +
							"      A.AUTHOR_ID," +
							"      A.NAME AS AUTHOR_NAME," +
							"      A.SURNAME," +
							"      G.GENRE_ID," +
							"      G.NAME AS GENRE_NAME" +
							" FROM BOOKS B, " +
								"AUTHORS A, " +
								"GENRES G" +
							" WHERE B.BOOK_ID = :bookId" +
							" AND B.AUTHOR_ID = A.AUTHOR_ID" +
							" AND B.GENRE_ID = G.GENRE_ID",
					Map.of("bookId", id),
					new BookRowMapper()
			);
			return Optional.ofNullable(book);
		} catch (DataAccessException e) {
			logger.error("error getting book by id: {}", id, e);
			return Optional.empty();
		}
	}

	@Override
	public List<Book> findAllBooks() {
		try {
			return jdbcOperations.query(
					"SELECT B.BOOK_ID," +
							"      B.NAME AS BOOK_NAME," +
							"      B.PUBLICATION_DATE," +
							"      B.PRINT_LENGTH," +
							"      A.AUTHOR_ID," +
							"      A.NAME AS AUTHOR_NAME," +
							"      A.SURNAME," +
							"      G.GENRE_ID," +
							"      G.NAME AS GENRE_NAME" +
							" FROM BOOKS B, " +
								"AUTHORS A, " +
								"GENRES G" +
							" WHERE B.AUTHOR_ID = A.AUTHOR_ID" +
							" AND B.GENRE_ID = G.GENRE_ID",
					Collections.emptyMap(),
					new BookRowMapper()
			);
		} catch (DataAccessException e) {
			logger.error("error getting all books", e);
			return Collections.emptyList();
		}
	}

	@Override
	public void removeBook(final long id) {
		try {
			jdbcOperations.update(
					"DELETE FROM BOOKS WHERE BOOK_ID = :bookId",
					Map.of("bookId", id)
			);
		} catch (DataAccessException e) {
			logger.error("error removing book by id: {}", id, e);
		}
	}

	@Override
	public void updateBook(final Book book) {
		try {
			jdbcOperations.update(
					"UPDATE BOOKS B SET B.NAME = :name, B.PRINT_LENGTH = :printLength," +
							"B.PUBLICATION_DATE = :publicationDate WHERE B.BOOK_ID = :bookId",
					Map.of("bookId", book.getId(),
							"name", book.getName(),
							"printLength", book.getPrintLength(),
							"publicationDate", book.getPublicationDate())
			);
		} catch (DataAccessException e) {
			logger.error("error updating book: {}", book, e);
		}
	}

	private static class BookRowMapper implements RowMapper<Book>{

		@Override
		public Book mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			final long bookId = rs.getLong("BOOK_ID");
			final String bookName = rs.getString("BOOK_NAME");
			final Date publicationDate = rs.getDate("PUBLICATION_DATE");
			final int printLength = rs.getInt("PRINT_LENGTH");
			final long authorId = rs.getLong("AUTHOR_ID");
			final String authorName = rs.getString("AUTHOR_NAME");
			final String authorSurname = rs.getString("SURNAME");
			final long genreId = rs.getLong("GENRE_ID");
			final String genreName = rs.getString("GENRE_NAME");
			final Author author = new Author(authorId, authorName, authorSurname);
			final Genre genre = new Genre(genreId, genreName);

			return new Book(bookId, bookName, publicationDate, printLength, author, genre);
		}
	}
}
