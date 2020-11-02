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
import ru.otus.domain.Author;
import ru.otus.domain.Book;
import ru.otus.domain.Genre;
import ru.otus.exceptions.BooksServiceException;
import ru.otus.repositories.BooksRepository;
import ru.otus.validators.FieldValidator;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
	private static final int NAME_LENGTH_GREATER_THAN_MAX_LENGTH = 300;

	@Configuration
	@Import(BooksServiceImpl.class)
	@EnableConfigurationProperties(AppProperties.class)
	public static class BooksServiceConfig {

	}

	@MockBean
	private FieldValidator fieldValidator;

	@MockBean
	private BooksRepository booksRepository;

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
	@DisplayName("should throw BooksServiceException when book for save is null")
	public void shouldThrowExceptionWhenBookForSaveIsNull() {
		Book book = null;
		assertThrows(BooksServiceException.class, () -> booksService.save(book));
	}

	@ParameterizedTest
	@DisplayName("should throw BooksServiceException when book for save has not valid name")
	@NullAndEmptySource
	@MethodSource(value = "createNameGreaterThanMaxLength")
	public void shouldThrowExceptionWhenBookForSaveHasNotValidName(final String bookName) {
		var book = new Book.Builder()
				.name(bookName)
				.publicationDate(BOOK_PUBLICATION_DATE)
				.printLength(BOOK_PRINT_LENGTH)
				.author(author)
				.genre(genre)
				.build();

		given(fieldValidator.validate(book)).willReturn(false);
		assertThrows(BooksServiceException.class, () -> booksService.save(book));
	}

	@Test
	@DisplayName("should throw BooksServiceException when book for save has not valid publication date")
	public void shouldThrowExceptionWhenBookForSaveHasNotValidPublicationDate() {
		var book = new Book.Builder()
				.name(BOOK_NAME)
				.publicationDate(null)
				.printLength(BOOK_PRINT_LENGTH)
				.author(author)
				.genre(genre)
				.build();

		given(fieldValidator.validate(book)).willReturn(false);
		assertThrows(BooksServiceException.class, () -> booksService.save(book));
	}

	@ParameterizedTest
	@DisplayName("should throw BooksServiceException when book for save has not valid print length")
	@ValueSource(ints = {0, -1})
	public void shouldThrowExceptionWhenBookForSaveHasNotValidPrintLength(final int printLength) {
		var book = new Book.Builder()
				.name(BOOK_NAME)
				.publicationDate(BOOK_PUBLICATION_DATE)
				.printLength(printLength)
				.author(author)
				.genre(genre)
				.build();

		given(fieldValidator.validate(book)).willReturn(false);
		assertThrows(BooksServiceException.class, () -> booksService.save(book));
	}

	@Test
	@DisplayName("should throw BooksServiceException when book for save has not valid author")
	public void shouldThrowExceptionWhenBookForSaveHasNotValidAuthor() {
		var book = new Book.Builder()
				.name(BOOK_NAME)
				.publicationDate(BOOK_PUBLICATION_DATE)
				.printLength(BOOK_PRINT_LENGTH)
				.author(null)
				.genre(genre)
				.build();

		given(fieldValidator.validate(book)).willReturn(false);
		assertThrows(BooksServiceException.class, () -> booksService.save(book));
	}

	@Test
	@DisplayName("should throw BooksServiceException when book for save has not valid genre")
	public void shouldThrowExceptionWhenBookForSaveHasNotValidGenre() {
		var book = new Book.Builder()
				.name(BOOK_NAME)
				.publicationDate(BOOK_PUBLICATION_DATE)
				.printLength(BOOK_PRINT_LENGTH)
				.author(author)
				.genre(null)
				.build();

		given(fieldValidator.validate(book)).willReturn(false);
		assertThrows(BooksServiceException.class, () -> booksService.save(book));
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

		given(booksRepository.findById(BOOK_ID)).willReturn(Optional.of(book));
		assertThat(booksService.getById(BOOK_ID)).get().isEqualToComparingFieldByField(book);
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

		given(booksRepository.findAll()).willReturn(books);
		assertThat(booksService.getAll()).isNotEmpty().containsOnlyOnceElementsOf(books);
	}

	@Test
	@DisplayName("should remove book by id")
	public void shouldRemoveBookByIdWithoutThrows() {
		var expectedBook = new Book.Builder()
				.id(BOOK_ID)
				.name(BOOK_NAME)
				.publicationDate(BOOK_PUBLICATION_DATE)
				.printLength(BOOK_PRINT_LENGTH)
				.author(author)
				.genre(genre)
				.build();

		given(booksService.getById(BOOK_ID)).willReturn(Optional.of(expectedBook));

		assertDoesNotThrow(() -> booksService.deleteById(BOOK_ID));
	}

	private static String[] createNameGreaterThanMaxLength() {
		return new String[]{IntStream.range(0, NAME_LENGTH_GREATER_THAN_MAX_LENGTH)
				.mapToObj(String::valueOf)
				.collect(Collectors.joining())};
	}
}