package ru.otus.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.otus.domain.Author;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class AuthorsDaoJpa implements AuthorsDao {

	private static final Logger logger = LoggerFactory.getLogger(AuthorsDaoJpa.class);

	@PersistenceContext
	private EntityManager em;

	@Override
	public void save(final Author author) {
		try {
			if (author.getId() == null) {
				em.persist(author);
			} else {
				em.merge(author);
			}
		} catch (Exception e) {
			logger.error("error creating author: {}", author, e);
		}
	}

	@Override
	public Optional<Author> findById(final long id) {
		return Optional.ofNullable(em.find(Author.class, id));
	}

	@Override
	public List<Author> findAll() {
		try {
			final var query = em.createQuery("select a from Author a", Author.class);
			return query.getResultList();
		} catch (Exception e) {
			logger.error("error getting all authors", e);
			return Collections.emptyList();
		}
	}

	@Override
	public boolean remove(final long id) {
		try {
			final var query = em.createQuery("delete from Author a where a.id = :id");

			query.setParameter("id", id);

			return query.executeUpdate() > 0;
		} catch (Exception e) {
			logger.error("error removing author by id: {}", id, e);
			return false;
		}
	}
}
