package ru.otus.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.domain.Genre;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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

		genresDao.save(genre);
		assertNotNull(genre.getId());

		var savedGenre = em.find(Genre.class, genre.getId());

		assertThat(savedGenre).isNotNull().isEqualToComparingFieldByField(genre);
	}

	@Test
	@DisplayName("should return genre by id from data.sql")
	public void shouldReturnGenreById() {
		var genre = genresDao.findById(FIRST_GENRE_ID).orElseThrow();
		var expectedGenre = em.find(Genre.class, FIRST_GENRE_ID);

		assertThat(genre).isEqualToComparingFieldByField(expectedGenre);
	}

	@Test
	@DisplayName("should return all genres from data.sql")
	public void shouldReturnAllGenres() {
		var genres = genresDao.findAll();
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

		assertNotNull(genreForRemove);
		assertTrue(genresDao.removeById(FIRST_GENRE_ID));

		var removedGenre = em.find(Genre.class, FIRST_GENRE_ID);

		assertNull(removedGenre);
	}

	@Test
	@DisplayName("should update genre")
	public void shouldUpdateGenre() {
		var genre = em.find(Genre.class, FIRST_GENRE_ID);

		genre.setName(NEW_GENRE);
		em.detach(genre);
		genresDao.save(genre);

		var updatedGenre = em.find(Genre.class, FIRST_GENRE_ID);

		assertEquals(NEW_GENRE, updatedGenre.getName());
	}
}