package ru.otus.routers.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.domain.*;
import ru.otus.repositories.AuthorsRepository;
import ru.otus.repositories.BooksRepository;
import ru.otus.repositories.GenresRepository;
import ru.otus.validators.FieldValidator;

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest
@DisplayName("route for book comments")
class BooksRouterUnitTest {

	private static final String BOOK_NAME = "name";
	private static final String AUTHOR_ID = "1";
	private static final String GENRE_ID = "1";
	private static final String BOOK_ID = "1";
	private static final Date BOOK_PUBLICATION_DATE = new Date();
	private static final int BOOK_PRINT_LENGTH = 300;
	private static final String AUTHOR_NAME = "name";
	private static final String AUTHOR_SURNAME = "surname";
	private static final String GENRE_NAME = "name";
	private static final String BOOK_DOMAIN_URL = "/api/book/";
	private static final String BOOK_BY_ID_URL = BOOK_DOMAIN_URL + "{id}";

	@Configuration
	@Import(BooksRouter.class)
	public static class BooksRouterConfig {

	}

	@Autowired
	@Qualifier("booksRoute")
	private RouterFunction<ServerResponse> route;

	@MockBean
	private FieldValidator validator;

	@MockBean
	private BooksRepository booksRepository;

	@MockBean
	private AuthorsRepository authorsRepository;

	@MockBean
	private GenresRepository genresRepository;

	private WebTestClient testClient;

	private Gson gson;

	private Book book;

	@BeforeEach
	void setUp() {
		this.testClient = WebTestClient.bindToRouterFunction(route).build();
		this.gson = new GsonBuilder()
				.setDateFormat(Constants.DEFAULT_DATE_PATTERN)
				.create();
		this.book = new Book.Builder()
				.id(BOOK_ID)
				.name(BOOK_NAME)
				.publicationDate(BOOK_PUBLICATION_DATE)
				.printLength(BOOK_PRINT_LENGTH)
				.author(buildAuthor())
				.genre(buildGenre())
				.build();
	}


	@Test
	@DisplayName("should save book by POST HTTP request")
	public void shouldSaveBook() {
		var candidate = buildBookCandidate();

		given(authorsRepository.findById(candidate.getAuthorId())).willReturn(Mono.just(buildAuthor()));
		given(genresRepository.findById(candidate.getGenreId())).willReturn(Mono.just(buildGenre()));
		given(validator.validate(any())).willReturn(new BeanPropertyBindingResult(book, book.getClass().getName()));
		given(booksRepository.save(any())).willReturn(Mono.just(book));

		testClient.post()
				.uri(BOOK_DOMAIN_URL)
				.accept(APPLICATION_JSON)
				.bodyValue(candidate)
				.exchange()
				.expectStatus()
				.isCreated()
				.expectHeader()
				.contentType(APPLICATION_JSON)
				.expectBody()
				.json(gson.toJson(book));
	}

	@Test
	@DisplayName("should return all books by GET HTTP request")
	public void shouldReturnAllBooks() {
		var books = List.of(book);

		given(booksRepository.findAll()).willReturn(Flux.just(book));

		testClient.get()
				.uri(BOOK_DOMAIN_URL)
				.exchange()
				.expectStatus()
				.isOk()
				.expectHeader()
				.contentType(APPLICATION_JSON)
				.expectBody()
				.json(gson.toJson(books));
	}

	@Test
	@DisplayName("should return book by id by GET HTTP request")
	public void shouldReturnBookById() {
		given(booksRepository.findById(BOOK_ID)).willReturn(Mono.just(book));

		testClient.get()
				.uri(uriBuilder -> uriBuilder.path(BOOK_BY_ID_URL).build(BOOK_ID))
				.exchange()
				.expectStatus()
				.isOk()
				.expectHeader()
				.contentType(APPLICATION_JSON)
				.expectBody()
				.json(gson.toJson(book));
	}

	@Test
	@DisplayName("should update book by PUT HTTP request")
	public void shouldUpdateBook() {
		given(validator.validate(any())).willReturn(new BeanPropertyBindingResult(book, book.getClass().getName()));
		given(booksRepository.save(any())).willReturn(Mono.just(book));

		testClient.put()
				.uri(BOOK_DOMAIN_URL)
				.accept(APPLICATION_JSON)
				.bodyValue(book)
				.exchange()
				.expectStatus()
				.isNoContent();
	}

	@Test
	@DisplayName("should delete book by DELETE HTTP request")
	public void shouldDeleteBook() {
		given(booksRepository.deleteById(BOOK_ID)).willReturn(Mono.empty());

		testClient.delete()
				.uri(uriBuilder -> uriBuilder.path(BOOK_BY_ID_URL).build(BOOK_ID))
				.exchange()
				.expectStatus()
				.isNoContent();
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