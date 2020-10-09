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
import ru.otus.configs.AppProperties;
import ru.otus.domain.Author;
import ru.otus.domain.Genre;
import ru.otus.domain.dto.AuthorDto;
import ru.otus.domain.dto.BookDto;
import ru.otus.domain.dto.GenreDto;
import ru.otus.services.AuthorsService;
import ru.otus.services.BooksService;
import ru.otus.services.GenresService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;

@SpringBootTest
@DisplayName("facade for work with books")
class BooksFacadeImplUnitTest {

	public static final long AUTHOR_ID = 1L;
	public static final long GENRE_ID = 1L;
	public static final long BOOK_ID = 1L;

	@Configuration
	@Import(BooksFacadeImpl.class)
	@EnableConfigurationProperties(AppProperties.class)
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
		var author = new AuthorDto.Builder().id(AUTHOR_ID).build();
		var genre = new GenreDto.Builder().id(GENRE_ID).build();
		var book = new BookDto.Builder()
				.id(BOOK_ID)
				.genre(genre)
				.author(author)
				.build();
		given(authorsService.getById(AUTHOR_ID)).willReturn(Optional.of(new Author()));
		given(genresService.getById(GENRE_ID)).willReturn(Optional.of(new Genre()));

		booksFacade.save(book);

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
		booksFacade.removeById(BOOK_ID);
		inOrder.verify(booksService, times(1)).removeById(BOOK_ID);
	}
}