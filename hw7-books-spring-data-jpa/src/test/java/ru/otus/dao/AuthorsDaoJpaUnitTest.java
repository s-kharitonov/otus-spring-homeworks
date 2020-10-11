package ru.otus.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.domain.Author;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("DAO for work this authors")
@Import(AuthorsDaoJpa.class)
@DataJpaTest
class AuthorsDaoJpaUnitTest {

	private static final String NAME = "sergey";
	private static final String SURNAME = "kharitonov";
	private static final long FIRST_AUTHOR_ID = 1L;

	@Autowired
	private AuthorsDao authorsDao;

	@Autowired
	private TestEntityManager em;

	@Test
	@DisplayName("should create default author and return author id")
	public void shouldCreateAuthor() {
		var author = new Author.Builder()
				.name(NAME)
				.surname(SURNAME)
				.build();

		authorsDao.save(author);
		assertNotNull(author.getId());

		var savedAuthor = em.find(Author.class, author.getId());

		assertThat(savedAuthor).isNotNull().isEqualToComparingFieldByField(author);
	}

	@Test
	@DisplayName("should find author by id")
	public void shouldFindAuthorById() {
		var author = authorsDao.findById(FIRST_AUTHOR_ID).orElseThrow();
		var expectedAuthor  = em.find(Author.class, FIRST_AUTHOR_ID);

		assertThat(author).isEqualToComparingFieldByField(expectedAuthor);
	}

	@Test
	@DisplayName("should find all authors from data.sql")
	public void shouldFindAllAuthors() {
		var authors = authorsDao.findAll();
		var expectedAuthors = em.getEntityManager()
				.createQuery("select a from Author a", Author.class)
				.getResultList();

		assertThat(authors).containsOnlyOnceElementsOf(expectedAuthors);
	}

	@Test
	@DisplayName("should remove author by default id")
	public void shouldRemoveAuthorById() {
		var authorForRemove = em.find(Author.class, FIRST_AUTHOR_ID);

		assertNotNull(authorForRemove);
		em.detach(authorForRemove);
		authorsDao.remove(FIRST_AUTHOR_ID);

		var removedAuthor = em.find(Author.class, FIRST_AUTHOR_ID);

		assertNull(removedAuthor);
	}

	@Test
	@DisplayName("should update author name and surname")
	public void shouldUpdateAuthorName() {
		var author = em.find(Author.class, FIRST_AUTHOR_ID);

		assertNotNull(author);
		author.setName(NAME);
		author.setSurname(SURNAME);
		em.detach(author);
		authorsDao.save(author);

		var updatedAuthor = em.find(Author.class, FIRST_AUTHOR_ID);

		assertThat(updatedAuthor).isNotNull().isEqualToComparingFieldByField(author);
	}
}