package ru.otus.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.domain.Author;
import ru.otus.domain.Book;
import ru.otus.domain.Genre;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@DisplayName("DAO for work with books")
@Import(BooksDaoJdbc.class)
class BooksDaoJdbcUnitTest {

	private static final long FIRST_AUTHOR_ID = 1L;
	private static final long FIRST_GENRE_ID = 1L;
	private static final String FIRST_AUTHOR_NAME = "Karen";
	private static final String FIRST_AUTHOR_SURNAME = "Osborne";
	private static final String FIRST_GENRE_NAME = "Fantasy";
	private static final String NEW_BOOK_NAME = "Harry Potter";
	private static final Date NEW_BOOK_PUBLICATION_DATE = new Date();
	private static final int NEW_BOOK_PRINT_LENGTH = 255;
	private static final long FIRST_BOOK_ID = 1L;

	@Autowired
	private BooksDao booksDao;

	@Test
	@DisplayName("should create book")
	public void shouldCreateBook() {
		var author = new Author.Builder()
				.id(FIRST_AUTHOR_ID)
				.name(FIRST_AUTHOR_NAME)
				.surname(FIRST_AUTHOR_SURNAME)
				.build();
		var genre = new Genre.Builder()
				.id(FIRST_GENRE_ID)
				.name(FIRST_GENRE_NAME)
				.build();
		var book = new Book.Builder()
				.name(NEW_BOOK_NAME)
				.publicationDate(NEW_BOOK_PUBLICATION_DATE)
				.printLength(NEW_BOOK_PRINT_LENGTH)
				.author(author)
				.genre(genre)
				.build();

		assertTrue(booksDao.saveBook(book).isPresent());
	}

	@Test
	@DisplayName("should throw NullPointerException when book for create is null")
	public void shouldThrowExceptionWhenBookForCreateIsNull() {
		assertThrows(NullPointerException.class, () -> booksDao.saveBook(null));
	}

	@Test
	@DisplayName("should return book by id from data.sql")
	public void shouldReturnBookById() {
		assertTrue(booksDao.findBookById(FIRST_BOOK_ID).isPresent());
	}

	@Test
	@DisplayName("should return all books from data.sql")
	public void shouldReturnAllBooks() {
		assertFalse(booksDao.findAllBooks().isEmpty());
	}

	@Test
	@DisplayName("should remove book from data.sql by id")
	public void shouldRemoveBook() {
		assertTrue(booksDao.removeBook(FIRST_BOOK_ID));
		assertTrue(booksDao.findBookById(FIRST_BOOK_ID).isEmpty());
	}

	@Test
	@DisplayName("should update book from data.sql")
	public void shouldUpdateBook() {
		var book = booksDao.findBookById(FIRST_BOOK_ID).orElseThrow();

		book.setName(NEW_BOOK_NAME);
		book.setPrintLength(NEW_BOOK_PRINT_LENGTH);

		assertTrue(booksDao.updateBook(book));

		var updatedBook = booksDao.findBookById(FIRST_BOOK_ID).orElseThrow();

		assertEquals(NEW_BOOK_NAME, updatedBook.getName());
		assertEquals(NEW_BOOK_PRINT_LENGTH, updatedBook.getPrintLength());
	}

	@Test
	@DisplayName("should throw NullPointerException when book for update is null")
	public void shouldThrowExceptionWhenBookForUpdateIsNull() {
		assertThrows(NullPointerException.class, () -> booksDao.updateBook(null));
	}
}