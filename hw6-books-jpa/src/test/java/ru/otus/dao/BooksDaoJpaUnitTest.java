package ru.otus.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.domain.Author;
import ru.otus.domain.Book;
import ru.otus.domain.Genre;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("DAO for work with books")
@Import(BooksDaoJpa.class)
class BooksDaoJpaUnitTest {

	private static final long AUTHOR_ID = 1L;
	private static final long GENRE_ID = 1L;
	private static final String NEW_BOOK_NAME = "Harry Potter";
	private static final Date NEW_BOOK_PUBLICATION_DATE = new Date();
	private static final int NEW_BOOK_PRINT_LENGTH = 255;
	private static final long BOOK_ID = 1L;

	@Autowired
	private BooksDao booksDao;

	@Autowired
	private TestEntityManager em;

	@Test
	@DisplayName("should create book")
	public void shouldCreateBook() {
		var author = em.find(Author.class, AUTHOR_ID);
		var genre = em.find(Genre.class, GENRE_ID);
		var book = new Book.Builder()
				.name(NEW_BOOK_NAME)
				.publicationDate(NEW_BOOK_PUBLICATION_DATE)
				.printLength(NEW_BOOK_PRINT_LENGTH)
				.author(author)
				.genre(genre)
				.build();

		booksDao.save(book);
		assertNotNull(book.getId());

		var savedBook = em.find(Book.class, book.getId());

		assertThat(savedBook).isNotNull().isEqualToComparingFieldByField(book);
	}

	@Test
	@DisplayName("should update book")
	public void shouldUpdateBook() {
		var book = em.find(Book.class, BOOK_ID);

		book.setName(NEW_BOOK_NAME);
		book.setPublicationDate(NEW_BOOK_PUBLICATION_DATE);
		book.setPrintLength(NEW_BOOK_PRINT_LENGTH);
		em.detach(book);
		booksDao.save(book);

		var updatedBook = em.find(Book.class, BOOK_ID);

		assertThat(updatedBook)
				.isNotNull()
				.isEqualToIgnoringGivenFields(book, "comments", "genre", "author");
	}

	@Test
	@DisplayName("should return book by id")
	public void shouldReturnBookById() {
		var book = booksDao.findById(BOOK_ID).orElseThrow();
		var expectedBook = em.find(Book.class, BOOK_ID);

		assertThat(book).isNotNull().isEqualToComparingFieldByField(expectedBook);
	}

	@Test
	@DisplayName("should return all books")
	public void shouldReturnAllBooks() {
		var books = booksDao.findAll();
		var expectedBooks = em.getEntityManager()
				.createQuery("select b from Book b", Book.class)
				.getResultList();

		assertThat(books).containsOnlyOnceElementsOf(expectedBooks);
	}

	@Test
	@DisplayName("should remove book by id")
	public void shouldRemoveBookById() {
		var book = em.find(Book.class, BOOK_ID);

		assertNotNull(book);
		em.detach(book);
		assertTrue(booksDao.removeById(book.getId()));

		var removedBook = em.find(Book.class, BOOK_ID);

		assertNull(removedBook);
	}
}