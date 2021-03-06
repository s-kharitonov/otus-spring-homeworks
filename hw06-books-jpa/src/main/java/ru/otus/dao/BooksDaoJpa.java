package ru.otus.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.otus.domain.Book;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class BooksDaoJpa implements BooksDao {

	private static final Logger logger = LoggerFactory.getLogger(BooksDaoJpa.class);

	@PersistenceContext
	private EntityManager em;

	@Override
	public void save(final Book book) {
		try {
			if (Objects.isNull(book.getId())) {
				em.persist(book);
				return;
			}

			em.merge(book);
		} catch (Exception e) {
			logger.error("error creating book: {}", book, e);
		}
	}

	@Override
	public Optional<Book> findById(final long id) {
		return Optional.ofNullable(em.find(Book.class, id));
	}

	@Override
	public List<Book> findAll() {
		try {
			final var query = em.createQuery(
					"select b from Book b join fetch b.author join fetch b.genre",
					Book.class
			);
			return query.getResultList();
		} catch (Exception e) {
			logger.error("error getting all books", e);
			return Collections.emptyList();
		}
	}

	@Override
	public boolean removeById(final long id) {
		try {
			final var query = em.createQuery("delete from Book b where b.id = :id");

			query.setParameter("id", id);

			return query.executeUpdate() > 0;
		} catch (Exception e) {
			logger.error("error removing book by id: {}", id, e);
			return false;
		}
	}
}
