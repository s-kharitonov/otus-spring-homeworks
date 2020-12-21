package ru.otus.services.facades;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.otus.domain.Author;
import ru.otus.domain.BookCandidate;
import ru.otus.domain.Genre;
import ru.otus.services.AuthorsService;
import ru.otus.services.BooksService;
import ru.otus.services.GenresService;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;

@SpringBootTest
@DisplayName("facade for books")
class BooksFacadeImplUnitTest {

	private static final String AUTHOR_ID = "1";
	private static final String GENRE_ID = "1";
	private static final String BOOK_ID = "1";
	private static final String BOOK_NAME = "Harry Potter";
	private static final Date BOOK_PUBLICATION_DATE = new Date();
	private static final int BOOK_PRINT_LENGTH = 255;
	private static final String AUTHOR_SURNAME = "kharitonov";
	private static final String AUTHOR_NAME = "sergey";
	private static final String GENRE_NAME = "Horror";

	@Configuration
	@Import(BooksFacadeImpl.class)
	@EnableConfigurationProperties
	public static class BooksFacadeImplConfig {

	}

	@MockBean
	private BooksService booksService;

	@MockBean
	private AuthorsService authorsService;

	@MockBean
	private GenresService genresService;

	@Autowired
	private BooksFacade booksFacade;

	private InOrder inOrder;

	@BeforeEach
	void setUp() {
		this.inOrder = inOrder(booksService, authorsService, genresService);
	}

	@Test
	@DisplayName("should call services for save book")
	public void shouldCallServicesForSaveBook() {
		var genre = new Genre.Builder()
				.id(GENRE_ID)
				.name(GENRE_NAME)
				.build();
		var author = new Author.Builder()
				.id(AUTHOR_ID)
				.name(AUTHOR_NAME)
				.surname(AUTHOR_SURNAME)
				.build();
		var bookCandidate = new BookCandidate.Builder()
				.bookId(BOOK_ID)
				.publicationDate(BOOK_PUBLICATION_DATE)
				.printLength(BOOK_PRINT_LENGTH)
				.name(BOOK_NAME)
				.genreId(GENRE_ID)
				.authorId(AUTHOR_ID)
				.build();

		given(authorsService.getById(AUTHOR_ID)).willReturn(Optional.ofNullable(author));
		given(genresService.getById(GENRE_ID)).willReturn(Optional.ofNullable(genre));
		assertDoesNotThrow(() -> booksFacade.save(bookCandidate));

		inOrder.verify(authorsService, times(1)).getById(AUTHOR_ID);
		inOrder.verify(genresService, times(1)).getById(GENRE_ID);
		inOrder.verify(booksService, times(1)).save(any());
	}

	@Test
	@DisplayName("should call service for get book by id")
	public void shouldCallServiceForGetBookById() {
		assertDoesNotThrow(() -> booksFacade.getById(BOOK_ID));
		inOrder.verify(booksService, times(1)).getById(BOOK_ID);
	}

	@Test
	@DisplayName("should call service for get all books")
	public void shouldCallServiceForGetAllBooks() {
		assertDoesNotThrow(() -> booksFacade.getAll());
		inOrder.verify(booksService, times(1)).getAll();
	}

	@Test
	@DisplayName("should call service for delete book by id")
	public void shouldCallServiceForDeleteBookById() {
		assertDoesNotThrow(() -> booksFacade.deleteById(BOOK_ID));
		inOrder.verify(booksService, times(1)).deleteById(BOOK_ID);
	}
}