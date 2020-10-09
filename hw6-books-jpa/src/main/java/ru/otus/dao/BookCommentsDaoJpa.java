package ru.otus.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.otus.domain.BookComment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Objects;

@Repository
public class BookCommentsDaoJpa implements BookCommentsDao {

	private static final Logger logger = LoggerFactory.getLogger(BookCommentsDaoJpa.class);

	@PersistenceContext
	private EntityManager em;

	@Override
	public void save(final BookComment comment) {
		try {
			if (Objects.isNull(comment.getId())) {
				em.persist(comment);
				return;
			}

			em.merge(comment);
		} catch (Exception e) {
			logger.error("error saving bookComment: {}", comment, e);
		}
	}
}
