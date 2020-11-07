package ru.otus.services.facades;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.otus.domain.Author;
import ru.otus.domain.Book;
import ru.otus.domain.BookCandidate;
import ru.otus.domain.Genre;
import ru.otus.services.AuthorsService;
import ru.otus.services.BooksService;
import ru.otus.services.GenresService;

import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;

@SpringBootTest
@DisplayName("facade for work with books")
class BooksFacadeImplUnitTest {

	private static final String BOOK_NAME = "name";
	private static final long AUTHOR_ID = 1L;
	private static final long GENRE_ID = 1L;
	private static final long BOOK_ID = 1L;
	private static final Date BOOK_PUBLICATION_DATE = new Date();
	private static final int BOOK_PRINT_LENGTH = 300;
	private static final String AUTHOR_NAME = "name";
	private static final String AUTHOR_SURNAME = "surname";
	private static final String GENRE_NAME = "name";

	@Configuration
	@Import(BooksFacadeImpl.class)
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
		var bookCandidate = buildBookCandidate();
		var expectedAuthor = new Author.Builder()
				.id(AUTHOR_ID)
				.name(AUTHOR_NAME)
				.surname(AUTHOR_SURNAME)
				.build();
		var expectedGenre = new Genre.Builder()
				.id(GENRE_ID)
				.name(GENRE_NAME)
				.build();
		var expectedBook = new Book.Builder()
				.id(BOOK_ID)
				.name(BOOK_NAME)
				.publicationDate(BOOK_PUBLICATION_DATE)
				.printLength(BOOK_PRINT_LENGTH)
				.author(expectedAuthor)
				.genre(expectedGenre)
				.build();
		given(authorsService.getById(AUTHOR_ID)).willReturn(Optional.of(expectedAuthor));
		given(genresService.getById(GENRE_ID)).willReturn(Optional.of(expectedGenre));
		given(booksService.save(any())).willReturn(expectedBook);

		booksFacade.create(bookCandidate);

		inOrder.verify(authorsService, times(1)).getById(AUTHOR_ID);
		inOrder.verify(genresService, times(1)).getById(GENRE_ID);
		inOrder.verify(booksService, times(1)).save(any());
	}

	@Test
	@DisplayName("should call service for get book by id")
	public void shouldCallServiceForGetBookById() {
		booksFacade.getById(BOOK_ID);
		inOrder.verify(booksService, times(1)).getById(BOOK_ID);
	}

	@Test
	@DisplayName("should call service for get all books")
	public void shouldCallServiceForGetAllBooks() {
		booksFacade.getAll();
		inOrder.verify(booksService, times(1)).getAll();
	}

	@Test
	@DisplayName("should call service for remove book by id")
	public void shouldCallServiceForRemoveBookById() {
		booksFacade.deleteById(BOOK_ID);
		inOrder.verify(booksService, times(1)).deleteById(BOOK_ID);
	}

	@Test
	@DisplayName("should call service for update book")
	public void shouldCallServiceForUpdateBook() {
		booksFacade.update(new Book());
		inOrder.verify(booksService, times(1)).save(any());
	}

	private BookCandidate buildBookCandidate() {
		var candidate = new BookCandidate();

		candidate.setAuthorId(AUTHOR_ID);
		candidate.setGenreId(GENRE_ID);
		candidate.setName(BOOK_NAME);
		candidate.setPublicationDate(BOOK_PUBLICATION_DATE);
		candidate.setPrintLength(BOOK_PRINT_LENGTH);

		return candidate;
	}
}