package ru.otus.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.domain.Author;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
		var authorId = authorsDao.saveAuthor(author).orElseThrow();

		assertNotNull(authorId);

		var savedAuthor = em.find(Author.class, authorId);

		assertNotNull(savedAuthor);
		assertEquals(NAME, savedAuthor.getName());
		assertEquals(SURNAME, savedAuthor.getSurname());
	}

	@Test
	@DisplayName("should find author by id")
	public void shouldFindAuthorById() {
		var author = authorsDao.findAuthorById(FIRST_AUTHOR_ID).orElseThrow();
		var expectedAuthor  = em.find(Author.class, FIRST_AUTHOR_ID);

		assertThat(author).isEqualToComparingFieldByField(expectedAuthor);
	}

	@Test
	@DisplayName("should find all authors from data.sql")
	public void shouldFindAllAuthors() {
		var authors = authorsDao.findAllAuthors();
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
		authorsDao.removeAuthor(FIRST_AUTHOR_ID);

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
		authorsDao.saveAuthor(author);

		var updatedAuthor = em.find(Author.class, FIRST_AUTHOR_ID);

		assertEquals(updatedAuthor.getName(), NAME);
		assertEquals(updatedAuthor.getSurname(), SURNAME);
	}

	@Test
	@DisplayName("should return empty value when author is null")
	public void shouldReturnEmptyValueWhenAuthorIsNull() {
		assertEquals(Optional.empty(), authorsDao.saveAuthor(null));
	}
}