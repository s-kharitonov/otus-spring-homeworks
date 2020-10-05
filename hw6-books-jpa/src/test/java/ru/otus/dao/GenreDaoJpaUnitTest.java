package ru.otus.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.domain.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("DAO for work this genres")
@Import(GenreDaoJpa.class)
@DataJpaTest
class GenreDaoJpaUnitTest {

	private static final String NEW_GENRE = "Horror";
	private static final long FIRST_GENRE_ID = 1L;

	@Autowired
	private GenresDao genresDao;

	@Autowired
	private TestEntityManager em;

	@Test
	@DisplayName("should save genre")
	public void shouldSaveGenre() {
		var genre = new Genre.Builder().name(NEW_GENRE).build();
		assertTrue(genresDao.saveGenre(genre).isPresent());
	}

	@Test
	@DisplayName("should return genre by id from data.sql")
	public void shouldReturnGenreById() {
		Optional<Genre> genre = genresDao.findGenreById(FIRST_GENRE_ID);
		var expectedGenre = em.find(Genre.class, FIRST_GENRE_ID);

		assertThat(genre).get().isEqualToComparingFieldByField(expectedGenre);
	}

	@Test
	@DisplayName("should return all genres from data.sql")
	public void shouldReturnAllGenres() {
		List<Genre> genres = genresDao.findAllGenres();
		var expectedGenres = em.getEntityManager()
				.createQuery("select g from Genre g", Genre.class)
				.getResultList();
		assertThat(genres).containsOnlyOnceElementsOf(expectedGenres);
	}

	@Test
	@DisplayName("should remove genre from data.sql by id")
	public void shouldRemoveGenreById() {
		var genreForRemove = em.find(Genre.class, FIRST_GENRE_ID);

		em.detach(genreForRemove);

		assertThat(genreForRemove).isNotNull();
		genresDao.removeGenre(FIRST_GENRE_ID);

		var removedGenre = em.find(Genre.class, FIRST_GENRE_ID);

		assertThat(removedGenre).isNull();
	}

	@Test
	@DisplayName("should update genre")
	public void shouldUpdateGenre() {
		var genre = em.find(Genre.class, FIRST_GENRE_ID);

		genre.setName(NEW_GENRE);
		em.detach(genre);
		genresDao.saveGenre(genre);

		var updatedGenre = em.find(Genre.class, FIRST_GENRE_ID);

		assertEquals(NEW_GENRE, updatedGenre.getName());
	}

	@Test
	@DisplayName("should return empty value when genre for save is null")
	public void shouldReturnEmptyValueWhenGenreForSaveIsNull() {
		assertEquals(Optional.empty(), genresDao.saveGenre(null));
	}
}