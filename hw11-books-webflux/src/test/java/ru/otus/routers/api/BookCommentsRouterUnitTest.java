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
import ru.otus.repositories.BookCommentsRepository;
import ru.otus.repositories.BooksRepository;
import ru.otus.validators.FieldValidator;

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest
@DisplayName("route for book comments")
class BookCommentsRouterUnitTest {

	private static final String BOOK_NAME = "name";
	private static final String AUTHOR_ID = "1";
	private static final String GENRE_ID = "1";
	private static final String BOOK_ID = "1";
	private static final Date BOOK_PUBLICATION_DATE = new Date();
	private static final int BOOK_PRINT_LENGTH = 300;
	private static final String AUTHOR_NAME = "name";
	private static final String AUTHOR_SURNAME = "surname";
	private static final String GENRE_NAME = "name";
	private static final String BOOK_COMMENT_ID = "1";
	private static final String BOOK_COMMENT = "comment";
	private static final String BOOK_COMMENT_DOMAIN_URL = "/api/book/comment/";
	private static final String BOOK_COMMENT_BY_ID_URL = BOOK_COMMENT_DOMAIN_URL + "{id}";

	@Configuration
	@Import(BookCommentsRouter.class)
	public static class BookCommentsRouterConfig {

	}

	@Autowired
	@Qualifier("bookCommentsRoute")
	private RouterFunction<ServerResponse> route;

	@MockBean
	private FieldValidator validator;

	@MockBean
	private BookCommentsRepository commentsRepository;

	@MockBean
	private BooksRepository booksRepository;

	private WebTestClient testClient;

	private Gson gson;

	private BookComment comment;

	@BeforeEach
	void setUp() {
		this.testClient = WebTestClient.bindToRouterFunction(route).build();
		this.gson = new GsonBuilder()
				.setDateFormat(Constants.DEFAULT_DATE_PATTERN)
				.create();
		this.comment = new BookComment.Builder()
				.id(BOOK_COMMENT_ID)
				.book(buildBook())
				.text(BOOK_COMMENT)
				.build();
	}

	@Test
	@DisplayName("should save comment by POST HTTP request")
	public void shouldSaveComment() {
		var commentCandidate = buildCommentCandidate();

		given(booksRepository.findById(commentCandidate.getBookId())).willReturn(Mono.just(buildBook()));
		given(validator.validate(any())).willReturn(new BeanPropertyBindingResult(comment, comment.getClass().getName()));
		given(commentsRepository.save(any())).willReturn(Mono.just(comment));

		testClient.post()
				.uri(BOOK_COMMENT_DOMAIN_URL)
				.accept(APPLICATION_JSON)
				.bodyValue(commentCandidate)
				.exchange()
				.expectStatus()
				.isCreated()
				.expectHeader()
				.contentType(APPLICATION_JSON)
				.expectBody()
				.json(gson.toJson(comment));
	}

	@Test
	@DisplayName("should return all comments by GET HTTP request")
	public void shouldReturnAllComments() {
		var comments = List.of(comment);

		given(commentsRepository.findAll()).willReturn(Flux.just(comment));

		testClient.get()
				.uri(BOOK_COMMENT_DOMAIN_URL)
				.exchange()
				.expectStatus()
				.isOk()
				.expectHeader()
				.contentType(APPLICATION_JSON)
				.expectBody()
				.json(gson.toJson(comments));
	}

	@Test
	@DisplayName("should return comment by id by GET HTTP request")
	public void shouldReturnCommentById() {
		given(commentsRepository.findById(BOOK_COMMENT_ID)).willReturn(Mono.just(comment));

		testClient.get()
				.uri(uriBuilder -> uriBuilder.path(BOOK_COMMENT_BY_ID_URL).build(BOOK_COMMENT_ID))
				.exchange()
				.expectStatus()
				.isOk()
				.expectHeader()
				.contentType(APPLICATION_JSON)
				.expectBody()
				.json(gson.toJson(comment));
	}

	@Test
	@DisplayName("should update comment by PUT HTTP method")
	public void shouldUpdateComment() {
		given(commentsRepository.save(any())).willReturn(Mono.just(comment));
		given(validator.validate(any())).willReturn(new BeanPropertyBindingResult(comment, comment.getClass().getName()));

		testClient.put()
				.uri(BOOK_COMMENT_DOMAIN_URL)
				.accept(APPLICATION_JSON)
				.bodyValue(comment)
				.exchange()
				.expectStatus()
				.isNoContent();
	}

	@Test
	@DisplayName("should delete comment by DELETE HTTP method")
	public void shouldDeleteComment() {
		given(commentsRepository.deleteById(BOOK_COMMENT_ID)).willReturn(Mono.empty());

		testClient.delete()
				.uri(uriBuilder -> uriBuilder.path(BOOK_COMMENT_BY_ID_URL).build(BOOK_COMMENT_ID))
				.exchange()
				.expectStatus()
				.isNoContent();
	}

	private BookCommentCandidate buildCommentCandidate() {
		var commentCandidate = new BookCommentCandidate();

		commentCandidate.setText(BOOK_COMMENT);
		commentCandidate.setBookId(BOOK_ID);

		return commentCandidate;
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

	private Book buildBook() {
		return new Book.Builder()
				.id(BOOK_ID)
				.name(BOOK_NAME)
				.publicationDate(BOOK_PUBLICATION_DATE)
				.printLength(BOOK_PRINT_LENGTH)
				.author(buildAuthor())
				.genre(buildGenre())
				.build();
	}
}