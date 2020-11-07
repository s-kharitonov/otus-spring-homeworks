package ru.otus.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.domain.Author;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DAO for work this authors")
@Import(AuthorsDaoJdbc.class)
@JdbcTest
class AuthorsDaoJdbcUnitTest {

	private static final String NAME = "sergey";
	private static final String SURNAME = "kharitonov";
	private static final long FIRST_AUTHOR_ID = 1L;

	@Autowired
	private AuthorsDao authorsDao;

	@Test
	@DisplayName("should create default author and return author id")
	public void shouldCreateAuthor() {
		var author = new Author.Builder()
				.name(NAME)
				.surname(SURNAME)
				.build();
		assertTrue(authorsDao.saveAuthor(author).isPresent());
	}

	@Test
	@DisplayName("should find author by id")
	public void shouldFindAuthorById() {
		assertTrue(authorsDao.findAuthorById(FIRST_AUTHOR_ID).isPresent());
	}

	@Test
	@DisplayName("should find all authors from data.sql")
	public void shouldFindAllAuthors() {
		assertFalse(authorsDao.findAllAuthors().isEmpty());
	}

	@Test
	@DisplayName("should remove author by default id")
	public void shouldRemoveAuthorById() {
		assertTrue(authorsDao.removeAuthor(FIRST_AUTHOR_ID));
		assertTrue(authorsDao.findAuthorById(FIRST_AUTHOR_ID).isEmpty());
	}

	@Test
	@DisplayName("should update author name and surname")
	public void shouldUpdateAuthorName() {
		var author = authorsDao.findAuthorById(FIRST_AUTHOR_ID).orElseThrow();

		author.setName(NAME);
		author.setSurname(SURNAME);

		assertTrue(authorsDao.updateAuthor(author));

		var updatedAuthor = authorsDao.findAuthorById(FIRST_AUTHOR_ID).orElseThrow();

		assertEquals(updatedAuthor.getName(), NAME);
		assertEquals(updatedAuthor.getSurname(), SURNAME);
	}

	@Test
	@DisplayName("should throw NullPointerException when author is null")
	public void shouldThrowExceptionWhenCreatedAuthorIsNull() {
		assertThrows(NullPointerException.class, () -> authorsDao.saveAuthor(null));
	}

	@Test
	@DisplayName("should throw NullPointerException when author is null")
	public void shouldThrowExceptionWhenUpdatedAuthorIsNull() {
		assertThrows(NullPointerException.class, () -> authorsDao.updateAuthor(null));
	}
}