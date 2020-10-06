package ru.otus.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.otus.domain.Comment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class CommentsDaoJpa implements CommentsDao {

	private static final Logger logger = LoggerFactory.getLogger(CommentsDaoJpa.class);

	@PersistenceContext
	private EntityManager em;

	@Override
	public void save(final Comment comment) {
		try {
			if (Objects.isNull(comment.getId())) {
				em.persist(comment);
				return;
			}

			em.merge(comment);
		} catch (Exception e) {
			logger.error("error saving comment: {}", comment, e);
		}
	}

	@Override
	public Optional<Comment> findById(final long id) {
		return Optional.ofNullable(em.find(Comment.class, id));
	}

	@Override
	public List<Comment> findAll() {
		try {
			final var query = em.createQuery("select c from Comment c", Comment.class);
			return query.getResultList();
		} catch (Exception e) {
			logger.error("error getting all comments", e);
			return Collections.emptyList();
		}
	}

	@Override
	public boolean removeById(final long id) {
		try {
			final var query = em.createQuery("delete from Comment c where c.id = :id");

			query.setParameter("id", id);

			return query.executeUpdate() > 0;
		} catch (Exception e) {
			logger.error("error removing genre by id: {}", id, e);
			return false;
		}
	}
}
