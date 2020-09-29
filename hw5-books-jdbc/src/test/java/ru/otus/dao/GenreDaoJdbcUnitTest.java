package ru.otus.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.domain.Genre;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DAO for work this genres")
@Import(GenreDaoJdbc.class)
@JdbcTest
class GenreDaoJdbcUnitTest {

	private static final String NEW_GENRE = "Horror";
	private static final long FIRST_GENRE_ID = 1L;

	@Autowired
	private GenresDao genresDao;

	@Test
	@DisplayName("should save genre")
	public void shouldSaveGenre() {
		var genre = new Genre.Builder().name(NEW_GENRE).build();
		assertTrue(genresDao.saveGenre(genre).isPresent());
	}

	@Test
	@DisplayName("should return genre by id from data.sql")
	public void shouldReturnGenreById() {
		assertTrue(genresDao.findGenreById(FIRST_GENRE_ID).isPresent());
	}

	@Test
	@DisplayName("should return all genres from data.sql")
	public void shouldReturnAllGenres() {
		assertFalse(genresDao.findAllGenres().isEmpty());
	}

	@Test
	@DisplayName("should remove genre from data.sql by id")
	public void shouldRemoveGenreById() {
		genresDao.removeGenre(FIRST_GENRE_ID);
		assertTrue(genresDao.findGenreById(FIRST_GENRE_ID).isEmpty());
	}

	@Test
	@DisplayName("should update genre")
	public void shouldUpdateGenre() {
		var genre = genresDao.findGenreById(FIRST_GENRE_ID).orElseThrow();

		genre.setName(NEW_GENRE);
		genresDao.updateGenre(genre);

		var updatedGenre = genresDao.findGenreById(FIRST_GENRE_ID).orElseThrow();

		assertEquals(NEW_GENRE, updatedGenre.getName());
	}

	@Test
	@DisplayName("should throw NullPointerException when genre for create is null")
	public void shouldThrowExceptionWhenGenreForCreateIsNull() {
		assertThrows(NullPointerException.class, () -> genresDao.saveGenre(null));
	}


	@Test
	@DisplayName("should throw NullPointerException when genre for update is null")
	public void shouldThrowExceptionWhenGenreForUpdateIsNull() {
		assertThrows(NullPointerException.class, () -> genresDao.updateGenre(null));
	}
}