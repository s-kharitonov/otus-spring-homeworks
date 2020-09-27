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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class AuthorsDaoJdbc implements AuthorsDao {

	private static final Logger logger = LoggerFactory.getLogger(AuthorsDaoJdbc.class);

	private final NamedParameterJdbcOperations jdbcOperations;

	public AuthorsDaoJdbc(final NamedParameterJdbcOperations jdbcOperations) {
		this.jdbcOperations = jdbcOperations;
	}

	@Override
	public Optional<Long> saveAuthor(final Author author) {
		final var keyHolder = new GeneratedKeyHolder();
		final var params = new MapSqlParameterSource();

		params.addValue("name", author.getName());
		params.addValue("surname", author.getSurname());

		try {
			jdbcOperations.update(
					"INSERT INTO AUTHORS (NAME, SURNAME) VALUES (:name, :surname)",
					params,
					keyHolder
			);

			return Optional.ofNullable((Long) keyHolder.getKey());
		} catch (DataAccessException e) {
			logger.error("error creating author: {}", author, e);
			return Optional.empty();
		}
	}

	@Override
	public Optional<Author> findAuthorById(final long id) {
		try {
			final Author author = jdbcOperations.queryForObject(
					"SELECT A.AUTHOR_ID, A.NAME, A.SURNAME FROM AUTHORS A WHERE  A.AUTHOR_ID = :authorId",
					Map.of("authorId", id),
					new AuthorRowMapper()
			);
			return Optional.ofNullable(author);
		} catch (DataAccessException e) {
			logger.error("error getting author by id: {}", id, e);
			return Optional.empty();
		}
	}

	@Override
	public List<Author> findAllAuthors() {
		try {
			return jdbcOperations.query(
					"SELECT A.AUTHOR_ID, A.NAME, A.SURNAME FROM AUTHORS A",
					Collections.emptyMap(),
					new AuthorRowMapper()
			);
		} catch (DataAccessException e) {
			logger.error("error getting all authors", e);
			return Collections.emptyList();
		}
	}

	@Override
	public void removeAuthor(final long id) {
		try {
			jdbcOperations.update(
					"DELETE FROM AUTHORS WHERE AUTHOR_ID = :authorId",
					Map.of("authorId", id)
			);
		} catch (DataAccessException e) {
			logger.error("error removing author by id: {}", id, e);
		}
	}

	@Override
	public void updateAuthor(final Author author) {
		try {
			jdbcOperations.update(
					"UPDATE AUTHORS A SET A.NAME = :name, A.SURNAME = :surname WHERE A.AUTHOR_ID = :authorId",
					Map.of("authorId", author.getId(),
							"name", author.getName(),
							"surname", author.getSurname())
			);
		} catch (DataAccessException e) {
			logger.error("error updating author: {}", author, e);
		}
	}

	private static class AuthorRowMapper implements RowMapper<Author> {

		@Override
		public Author mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			final long id = rs.getLong("AUTHOR_ID");
			final String name = rs.getString("NAME");
			final String surname = rs.getString("SURNAME");

			return new Author(id, name, surname);
		}
	}
}
