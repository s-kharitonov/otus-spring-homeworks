package ru.otus.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.otus.configs.AppProperties;
import ru.otus.dao.BooksDao;
import ru.otus.domain.Author;
import ru.otus.domain.Book;
import ru.otus.domain.Genre;
import ru.otus.exceptions.BooksServiceException;
import ru.otus.validators.FieldValidator;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@DisplayName("service for work with books")
class BooksServiceImplUnitTest {

	private static final String GENRE_NAME = "Horror";
	private static final long GENRE_ID = 1L;
	private static final String AUTHOR_SURNAME = "kharitonov";
	private static final String AUTHOR_NAME = "sergey";
	private static final long AUTHOR_ID = 1L;
	private static final String BOOK_NAME = "Harry Potter";
	private static final Date BOOK_PUBLICATION_DATE = new Date();
	private static final int BOOK_PRINT_LENGTH = 255;
	private static final long BOOK_ID = 1L;
	private static final String EMPTY_APP_MESSAGE = "";
	private static final int NAME_LENGTH_GREATER_THAN_MAX_LENGTH = 300;

	@Configuration
	@Import(BooksServiceImpl.class)
	@EnableConfigurationProperties(AppProperties.class)
	public static class BooksServiceConfig {

	}

	@MockBean
	private FieldValidator fieldValidator;

	@MockBean
	private BooksDao booksDao;

	@Autowired
	private BooksService booksService;

	private Genre genre;
	private Author author;

	@BeforeEach
	void setUp() {
		this.genre = new Genre.Builder()
				.id(GENRE_ID)
				.name(GENRE_NAME)
				.build();
		this.author = new Author.Builder()
				.id(AUTHOR_ID)
				.name(AUTHOR_NAME)
				.surname(AUTHOR_SURNAME)
				.build();
	}

	@Test
	@DisplayName("should save book")
	public void shouldSaveBook() {
		var book = new Book.Builder()
				.name(BOOK_NAME)
				.publicationDate(BOOK_PUBLICATION_DATE)
				.printLength(BOOK_PRINT_LENGTH)
				.author(author)
				.genre(genre)
				.build();

		given(fieldValidator.validate(book)).willReturn(true);
		given(booksDao.saveBook(book)).willReturn(Optional.of(BOOK_ID));

		var bookId = booksService.createBook(book).orElseThrow();

		assertEquals(BOOK_ID, bookId);
	}

	@Test
	@DisplayName("should throw BooksServiceException when book for create is null")
	public void shouldThrowExceptionWhenBookForCreateIsNull() {
		Book book = null;
		assertThrows(BooksServiceException.class, () -> booksService.createBook(book));
	}

	@ParameterizedTest
	@DisplayName("should throw BooksServiceException when book for create has not valid name")
	@NullAndEmptySource
	@MethodSource(value = "createNameGreaterThanMaxLength")
	public void shouldThrowExceptionWhenBookForCreateHasNotValidName(final String bookName) {
		var book = new Book.Builder()
				.name(bookName)
				.publicationDate(BOOK_PUBLICATION_DATE)
				.printLength(BOOK_PRINT_LENGTH)
				.author(author)
				.genre(genre)
				.build();

		given(fieldValidator.validate(book)).willReturn(false);
		assertThrows(BooksServiceException.class, () -> booksService.createBook(book));
	}

	@Test
	@DisplayName("should throw BooksServiceException when book for create has not valid publication date")
	public void shouldThrowExceptionWhenBookForCreateHasNotValidPublicationDate() {
		var book = new Book.Builder()
				.name(BOOK_NAME)
				.publicationDate(null)
				.printLength(BOOK_PRINT_LENGTH)
				.author(author)
				.genre(genre)
				.build();

		given(fieldValidator.validate(book)).willReturn(false);
		assertThrows(BooksServiceException.class, () -> booksService.createBook(book));
	}

	@ParameterizedTest
	@DisplayName("should throw BooksServiceException when book for create has not valid publication date")
	@ValueSource(ints = {0, -1})
	public void shouldThrowExceptionWhenBookForCreateHasNotValidPrintLength(final int printLength) {
		var book = new Book.Builder()
				.name(BOOK_NAME)
				.publicationDate(BOOK_PUBLICATION_DATE)
				.printLength(printLength)
				.author(author)
				.genre(genre)
				.build();

		given(fieldValidator.validate(book)).willReturn(false);
		assertThrows(BooksServiceException.class, () -> booksService.createBook(book));
	}

	@Test
	@DisplayName("should throw BooksServiceException when book for create has not valid author")
	public void shouldThrowExceptionWhenBookForCreateHasNotValidAuthor() {
		var book = new Book.Builder()
				.name(BOOK_NAME)
				.publicationDate(BOOK_PUBLICATION_DATE)
				.printLength(BOOK_PRINT_LENGTH)
				.author(null)
				.genre(genre)
				.build();

		given(fieldValidator.validate(book)).willReturn(false);
		assertThrows(BooksServiceException.class, () -> booksService.createBook(book));
	}

	@Test
	@DisplayName("should throw BooksServiceException when book for create has not valid genre")
	public void shouldThrowExceptionWhenBookForCreateHasNotValidGenre() {
		var book = new Book.Builder()
				.name(BOOK_NAME)
				.publicationDate(BOOK_PUBLICATION_DATE)
				.printLength(BOOK_PRINT_LENGTH)
				.author(author)
				.genre(null)
				.build();

		given(fieldValidator.validate(book)).willReturn(false);
		assertThrows(BooksServiceException.class, () -> booksService.createBook(book));
	}

	@Test
	@DisplayName("should return book by id")
	public void shouldReturnBookById() {
		var book = new Book.Builder()
				.id(BOOK_ID)
				.name(BOOK_NAME)
				.publicationDate(BOOK_PUBLICATION_DATE)
				.printLength(BOOK_PRINT_LENGTH)
				.author(author)
				.genre(genre)
				.build();

		given(booksDao.findBookById(BOOK_ID)).willReturn(Optional.of(book));
		assertEquals(BOOK_ID, booksService.getBookById(BOOK_ID).orElseThrow().getId());
	}

	@Test
	@DisplayName("should return all books")
	public void shouldReturnAllBooks() {
		var book = new Book.Builder()
				.id(BOOK_ID)
				.name(BOOK_NAME)
				.publicationDate(BOOK_PUBLICATION_DATE)
				.printLength(BOOK_PRINT_LENGTH)
				.author(author)
				.genre(genre)
				.build();
		var books = List.of(book);

		given(booksDao.findAllBooks()).willReturn(books);
		assertFalse(booksService.getAllBooks().isEmpty());
	}

	@Test
	@DisplayName("should remove book")
	public void shouldRemoveBook() {
		given(booksDao.removeBook(BOOK_ID)).willReturn(true);
		assertTrue(booksService.removeBook(BOOK_ID));
	}

	@Test
	@DisplayName("should update book")
	public void shouldUpdateBook() {
		var book = new Book.Builder()
				.id(BOOK_ID)
				.name(BOOK_NAME)
				.publicationDate(BOOK_PUBLICATION_DATE)
				.printLength(BOOK_PRINT_LENGTH)
				.author(author)
				.genre(genre)
				.build();

		given(fieldValidator.validate(book)).willReturn(true);
		given(booksDao.updateBook(book)).willReturn(true);
		assertTrue(booksService.updateBook(book));
	}

	@Test
	@DisplayName("should throw BooksServiceException when book for update is null")
	public void shouldThrowExceptionWhenBookForUpdateIsNull() {
		Book book = null;
		assertThrows(BooksServiceException.class, () -> booksService.updateBook(book));
	}

	@ParameterizedTest
	@DisplayName("should throw BooksServiceException when book for update has not valid name")
	@NullAndEmptySource
	@MethodSource(value = "createNameGreaterThanMaxLength")
	public void shouldThrowExceptionWhenBookForUpdateHasNotValidName(final String bookName) {
		var book = new Book.Builder()
				.name(bookName)
				.publicationDate(BOOK_PUBLICATION_DATE)
				.printLength(BOOK_PRINT_LENGTH)
				.author(author)
				.genre(genre)
				.build();

		given(fieldValidator.validate(book)).willReturn(false);
		assertThrows(BooksServiceException.class, () -> booksService.updateBook(book));
	}

	@Test
	@DisplayName("should throw BooksServiceException when book for update has not valid publication date")
	public void shouldThrowExceptionWhenBookForUpdateHasNotValidPublicationDate() {
		var book = new Book.Builder()
				.name(BOOK_NAME)
				.publicationDate(null)
				.printLength(BOOK_PRINT_LENGTH)
				.author(author)
				.genre(genre)
				.build();

		given(fieldValidator.validate(book)).willReturn(false);
		assertThrows(BooksServiceException.class, () -> booksService.updateBook(book));
	}

	@ParameterizedTest
	@DisplayName("should throw BooksServiceException when book for update has not valid publication date")
	@ValueSource(ints = {0, -1})
	public void shouldThrowExceptionWhenBookForUpdateHasNotValidPrintLength(final int printLength) {
		var book = new Book.Builder()
				.name(BOOK_NAME)
				.publicationDate(BOOK_PUBLICATION_DATE)
				.printLength(printLength)
				.author(author)
				.genre(genre)
				.build();

		given(fieldValidator.validate(book)).willReturn(false);
		assertThrows(BooksServiceException.class, () -> booksService.updateBook(book));
	}

	@Test
	@DisplayName("should throw BooksServiceException when book for update has not valid author")
	public void shouldThrowExceptionWhenBookForUpdateHasNotValidAuthor() {
		var book = new Book.Builder()
				.name(BOOK_NAME)
				.publicationDate(BOOK_PUBLICATION_DATE)
				.printLength(BOOK_PRINT_LENGTH)
				.author(null)
				.genre(genre)
				.build();

		given(fieldValidator.validate(book)).willReturn(false);
		assertThrows(BooksServiceException.class, () -> booksService.updateBook(book));
	}

	@Test
	@DisplayName("should throw BooksServiceException when book for update has not valid genre")
	public void shouldThrowExceptionWhenBookForUpdateHasNotValidGenre() {
		var book = new Book.Builder()
				.name(BOOK_NAME)
				.publicationDate(BOOK_PUBLICATION_DATE)
				.printLength(BOOK_PRINT_LENGTH)
				.author(author)
				.genre(null)
				.build();

		given(fieldValidator.validate(book)).willReturn(false);
		assertThrows(BooksServiceException.class, () -> booksService.updateBook(book));
	}

	private static String[] createNameGreaterThanMaxLength() {
		return new String[]{IntStream.range(0, NAME_LENGTH_GREATER_THAN_MAX_LENGTH)
				.mapToObj(String::valueOf)
				.collect(Collectors.joining())};
	}
}