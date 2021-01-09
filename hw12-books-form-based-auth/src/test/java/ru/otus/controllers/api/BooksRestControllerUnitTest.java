package ru.otus.controllers.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.domain.*;
import ru.otus.domain.dto.BookDto;
import ru.otus.services.facades.BooksFacade;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BooksRestController.class)
@DisplayName("controller for work with books")
class BooksRestControllerUnitTest {

	private static final long BOOK_ID = 1L;
	private static final String AUTHOR_NAME = "name";
	private static final String BOOK_NAME = "name";
	private static final Date BOOK_PUBLICATION_DATE = new Date();
	private static final int BOOK_PRINT_LENGTH = 200;
	private static final long AUTHOR_ID = 1L;
	private static final long GENRE_ID = 1L;
	private static final String AUTHOR_SURNAME = "surname";
	private static final String GENRE_NAME = "name";
	private static final String BOOK_DOMAIN_URL = "/api/book/";
	private static final String BOOK_BY_ID_URL = BOOK_DOMAIN_URL + "{id}";

	@Autowired
	private MockMvc mvc;

	@MockBean(name = "userDetailsServiceImpl")
	private UserDetailsService userDetailsService;

	@MockBean
	private BooksFacade booksFacade;

	private InOrder inOrder;

	private Gson gson;

	private Book expectedBook;

	@BeforeEach
	void setUp() {
		this.inOrder = inOrder(booksFacade);
		this.gson = new GsonBuilder()
				.setDateFormat(Constants.DEFAULT_DATE_PATTERN)
				.create();
		this.expectedBook = new Book.Builder()
				.id(BOOK_ID)
				.name(BOOK_NAME)
				.publicationDate(BOOK_PUBLICATION_DATE)
				.printLength(BOOK_PRINT_LENGTH)
				.author(buildAuthor())
				.genre(buildGenre())
				.build();
	}

	@Test
	@WithMockUser
	@DisplayName("should create book")
	public void shouldCreateBook() throws Exception {
		var candidate = buildBookCandidate();
		var bookDto = new BookDto(expectedBook);
		var requestBuilder = post(BOOK_DOMAIN_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(gson.toJson(candidate));

		given(booksFacade.create(any())).willReturn(bookDto);

		mvc.perform(requestBuilder)
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(gson.toJson(bookDto)));

		inOrder.verify(booksFacade, times(1)).create(any());
	}

	@Test
	@WithMockUser
	@DisplayName("should getting book by id")
	public void shouldGettingBookById() throws Exception {
		var bookDto = new BookDto(expectedBook);
		var requestBuilder = get(BOOK_BY_ID_URL, BOOK_ID)
				.contentType(MediaType.APPLICATION_JSON);

		given(booksFacade.getById(BOOK_ID)).willReturn(Optional.of(bookDto));

		mvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(gson.toJson(bookDto)));

		inOrder.verify(booksFacade, times(1)).getById(BOOK_ID);
	}

	@Test
	@WithMockUser
	@DisplayName("should return not found status code when book by id is not found!")
	public void shouldReturnNotFoundStatusCodeWhenBookByIdIsNotFound() throws Exception {
		var requestBuilder = get(BOOK_BY_ID_URL, BOOK_ID)
				.contentType(MediaType.APPLICATION_JSON);

		given(booksFacade.getById(BOOK_ID)).willReturn(Optional.empty());

		mvc.perform(requestBuilder)
				.andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}

	@Test
	@WithMockUser
	@DisplayName("should getting all books")
	public void shouldGettingAllBooks() throws Exception {
		var expectedBooks = List.of(new BookDto(expectedBook));
		var requestBuilder = get(BOOK_DOMAIN_URL)
				.contentType(MediaType.APPLICATION_JSON);

		given(booksFacade.getAll()).willReturn(expectedBooks);

		mvc.perform(requestBuilder)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(gson.toJson(expectedBooks)));

		inOrder.verify(booksFacade, times(1)).getAll();
	}

	@Test
	@WithMockUser
	@DisplayName("should delete book")
	public void shouldDeleteBook() throws Exception {
		var requestBuilder = delete(BOOK_BY_ID_URL, BOOK_ID)
				.contentType(MediaType.APPLICATION_JSON);

		mvc.perform(requestBuilder).andExpect(status().isNoContent());

		inOrder.verify(booksFacade, times(1)).deleteById(BOOK_ID);
	}

	@Test
	@WithMockUser
	@DisplayName("should update book")
	public void shouldUpdateBook() throws Exception {
		var requestBuilder = put(BOOK_DOMAIN_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(gson.toJson(expectedBook));

		mvc.perform(requestBuilder)
				.andExpect(status().isNoContent());

		inOrder.verify(booksFacade, times(1)).update(any());
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

	private Genre buildGenre() {
		return new Genre.Builder()
				.id(GENRE_ID)
				.name(GENRE_NAME)
				.build();
	}

	private Author buildAuthor() {
		return new Author.Builder()
				.id(AUTHOR_ID)
				.name(AUTHOR_NAME)
				.surname(AUTHOR_SURNAME)
				.build();
	}
}