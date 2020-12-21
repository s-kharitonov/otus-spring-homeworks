package ru.otus.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.otus.domain.Author;
import ru.otus.domain.Book;
import ru.otus.domain.Genre;
import ru.otus.exсeptions.BooksServiceException;
import ru.otus.repositories.BooksRepository;
import ru.otus.validators.FieldValidator;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@DisplayName("service for books")
@SpringBootTest
class BooksServiceImplUnitTest {
	private static final String GENRE_NAME = "Horror";
	private static final String GENRE_ID = "1";
	private static final String AUTHOR_SURNAME = "kharitonov";
	private static final String AUTHOR_NAME = "sergey";
	private static final String AUTHOR_ID = "1";
	private static final String BOOK_NAME = "Harry Potter";
	private static final Date BOOK_PUBLICATION_DATE = new Date();
	private static final int BOOK_PRINT_LENGTH = 255;
	private static final String BOOK_ID = "1";

	@Configuration
	@Import(BooksServiceImpl.class)
	@EnableConfigurationProperties
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
		assertThrows(BooksServiceException.class, () -> booksService.save(null));
	}

	@ParameterizedTest
	@DisplayName("should throw BooksServiceException when book for save has not valid name")
	@NullAndEmptySource
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
	@DisplayName("should throw BooksServiceException when books for save is null")
	public void shouldThrowExceptionWhenBooksForSaveIsNull() {
		assertThrows(BooksServiceException.class, () -> booksService.saveAll(null));
	}

	@Test
	@DisplayName("should throw BooksServiceException when books for save has invalid fields")
	public void shouldThrowExceptionWhenBooksForSaveHasInvalidFields() {
		var book = new Book.Builder()
				.name("")
				.publicationDate(null)
				.printLength(0)
				.author(null)
				.genre(null)
				.build();

		given(fieldValidator.validate(book)).willReturn(false);
		assertThrows(BooksServiceException.class, () -> booksService.saveAll(List.of(book)));
	}

	@Test
	@DisplayName("should save all books")
	public void shouldSaveAllBooks() {
		var book = new Book.Builder()
				.name(BOOK_NAME)
				.publicationDate(BOOK_PUBLICATION_DATE)
				.printLength(BOOK_PRINT_LENGTH)
				.author(author)
				.genre(genre)
				.build();
		var books = List.of(book);

		given(fieldValidator.validate(book)).willReturn(true);
		given(booksRepository.saveAll(books)).willReturn(books);
		assertThat(booksService.saveAll(books)).isNotEmpty().containsOnlyOnceElementsOf(books);
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
	@DisplayName("should return books by genre id")
	public void shouldReturnBooksByGenreId() {
		var book = new Book.Builder()
				.id(BOOK_ID)
				.name(BOOK_NAME)
				.publicationDate(BOOK_PUBLICATION_DATE)
				.printLength(BOOK_PRINT_LENGTH)
				.author(author)
				.genre(genre)
				.build();
		var books = List.of(book);

		given(booksRepository.findAllByGenre_Id(genre.getId())).willReturn(books);
		assertThat(booksService.getByGenreId(genre.getId())).isNotEmpty().containsOnlyOnceElementsOf(books);
	}

	@ParameterizedTest
	@NullAndEmptySource
	@DisplayName("should throw exception when genre id for get by genre id is not valid")
	public void shouldThrowExceptionWhenGenreIdForGetByGenreIdIsNotValid(final String genreId) {
		assertThrows(BooksServiceException.class, () -> booksService.getByGenreId(genreId));
	}

	@Test
	@DisplayName("should return books by author id")
	public void shouldReturnBooksByAuthorId() {
		var book = new Book.Builder()
				.id(BOOK_ID)
				.name(BOOK_NAME)
				.publicationDate(BOOK_PUBLICATION_DATE)
				.printLength(BOOK_PRINT_LENGTH)
				.author(author)
				.genre(genre)
				.build();
		var books = List.of(book);

		given(booksRepository.findAllByAuthor_Id(author.getId())).willReturn(books);
		assertThat(booksService.getByAuthorId(author.getId())).isNotEmpty().containsOnlyOnceElementsOf(books);
	}

	@ParameterizedTest
	@NullAndEmptySource
	@DisplayName("should throw exception when author id for get by author id is not valid")
	public void shouldThrowExceptionWhenAuthorIdForGetByAuthorIdIsNotValid(final String authorId) {
		assertThrows(BooksServiceException.class, () -> booksService.getByAuthorId(authorId));
	}

	@Test
	@DisplayName("should delete book by id")
	public void shouldDeleteBookByIdWithoutThrows() {
		assertDoesNotThrow(() -> booksService.deleteById(BOOK_ID));
	}

	@ParameterizedTest
	@NullAndEmptySource
	@DisplayName("should throw exception when book id for delete by book id is not valid")
	public void shouldThrowExceptionWhenBookIdForDeleteByIdIsNotValid(final String bookId) {
		assertThrows(BooksServiceException.class, () -> booksService.deleteById(bookId));
	}

	@Test
	@DisplayName("should delete book by author id")
	public void shouldDeleteBookByAuthorIdWithoutThrows() {
		assertDoesNotThrow(() -> booksService.deleteByAuthorId(AUTHOR_ID));
	}

	@ParameterizedTest
	@NullAndEmptySource
	@DisplayName("should throw exception when author id for delete by author id is not valid")
	public void shouldThrowExceptionWhenAuthorIdForDeleteByAuthorIdIsNotValid(final String authorId) {
		assertThrows(BooksServiceException.class, () -> booksService.deleteByAuthorId(authorId));
	}

	@Test
	@DisplayName("should delete book by genre id")
	public void shouldDeleteBookByGenreIdWithoutThrows() {
		assertDoesNotThrow(() -> booksService.deleteByGenreId(GENRE_ID));
	}

	@ParameterizedTest
	@NullAndEmptySource
	@DisplayName("should throw exception when genre id for delete by genre id is not valid")
	public void shouldThrowExceptionWhenGenreIdForDeleteByGenreIdIsNotValid(final String genreId) {
		assertThrows(BooksServiceException.class, () -> booksService.deleteByGenreId(genreId));
	}
}