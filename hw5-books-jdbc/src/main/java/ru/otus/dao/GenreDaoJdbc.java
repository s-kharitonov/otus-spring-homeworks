package ru.otus.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class GenreDaoJdbc implements GenresDao {

	private static final Logger logger = LoggerFactory.getLogger(GenreDaoJdbc.class);

	private final NamedParameterJdbcOperations jdbcOperations;

	public GenreDaoJdbc(final NamedParameterJdbcOperations jdbcOperations) {
		this.jdbcOperations = jdbcOperations;
	}

	@Override
	public Optional<Long> saveGenre(final Genre genre) {
		final var keyHolder = new GeneratedKeyHolder();
		final var params = new MapSqlParameterSource();

		params.addValue("name", genre.getName());

		try {
			jdbcOperations.update(
					"INSERT INTO GENRES (NAME) VALUES (:name)",
					params,
					keyHolder
			);

			return Optional.ofNullable((Long) keyHolder.getKey());
		} catch (DataAccessException e) {
			logger.error("error creating genre: {}", genre, e);
			return Optional.empty();
		}
	}

	@Override
	public Optional<Genre> findGenreById(final long id) {
		try {
			final Genre genre = jdbcOperations.queryForObject(
					"SELECT G.GENRE_ID, G.NAME FROM GENRES G WHERE  G.GENRE_ID = :genreId",
					Map.of("genreId", id),
					new GenreRowMapper()
			);
			return Optional.ofNullable(genre);
		} catch (DataAccessException e) {
			logger.error("error getting genre by id: {}", id, e);
			return Optional.empty();
		}
	}

	@Override
	public List<Genre> findAllGenres() {
		try {
			return jdbcOperations.query(
					"SELECT G.GENRE_ID, G.NAME FROM GENRES G",
					Collections.emptyMap(),
					new GenreRowMapper()
			);
		} catch (DataAccessException e) {
			logger.error("error getting all genres", e);
			return Collections.emptyList();
		}
	}

	@Override
	public void removeGenre(final long id) {
		try {
			jdbcOperations.update(
					"DELETE FROM GENRES WHERE GENRE_ID = :genreId",
					Map.of("genreId", id)
			);
		} catch (DataAccessException e) {
			logger.error("error removing genre by id: {}", id, e);
		}
	}

	@Override
	public void updateGenre(final Genre genre) {
		try {
			jdbcOperations.update(
					"UPDATE GENRES G SET G.NAME = :name WHERE G.GENRE_ID = :genreId",
					Map.of("genreId", genre.getId(), "name", genre.getName())
			);
		} catch (DataAccessException e) {
			logger.error("error updating genre: {}", genre, e);
		}
	}

	private static class GenreRowMapper implements RowMapper<Genre>{

		@Override
		public Genre mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			final long id = rs.getLong("GENRE_ID");
			final String name = rs.getString("NAME");

			return new Genre(id, name);
		}
	}
}
