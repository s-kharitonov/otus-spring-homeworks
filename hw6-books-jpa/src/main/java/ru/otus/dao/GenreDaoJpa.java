package ru.otus.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.otus.domain.Genre;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class GenreDaoJpa implements GenresDao {

	private static final Logger logger = LoggerFactory.getLogger(GenreDaoJpa.class);

	@PersistenceContext
	private EntityManager em;

	@Override
	public void saveGenre(final Genre genre) {
		try {
			if (genre.getId() == null) {
				em.persist(genre);
				return;
			}

			em.merge(genre);
		} catch (Exception e) {
			logger.error("error creating genre: {}", genre, e);
		}
	}

	@Override
	public Optional<Genre> findGenreById(final long id) {
		return Optional.ofNullable(em.find(Genre.class, id));
	}

	@Override
	public List<Genre> findAllGenres() {
		try {
			final var query = em.createQuery("select g from Genre g", Genre.class);
			return query.getResultList();
		} catch (Exception e) {
			logger.error("error getting all genres", e);
			return Collections.emptyList();
		}
	}

	@Override
	public boolean removeGenre(final long id) {
		try {
			final var query = em.createQuery("delete from Genre g where g.id = :id");

			query.setParameter("id", id);

			return query.executeUpdate() > 0;
		} catch (Exception e) {
			logger.error("error removing genre by id: {}", id, e);
			return false;
		}
	}
}
