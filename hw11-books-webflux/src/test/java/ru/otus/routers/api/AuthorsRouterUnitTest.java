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
import ru.otus.domain.Author;
import ru.otus.domain.AuthorDto;
import ru.otus.repositories.AuthorsRepository;
import ru.otus.validators.FieldValidator;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest
@DisplayName("route for authors")
class AuthorsRouterUnitTest {

	private static final String AUTHOR_ID = "1";
	private static final String AUTHOR_SURNAME = "surname";
	private static final String AUTHOR_NAME = "name";
	private static final String AUTHOR_DOMAIN_URL = "/api/author/";
	private static final String AUTHOR_BY_ID_URL = AUTHOR_DOMAIN_URL + "{id}";

	@Configuration
	@Import(AuthorsRouter.class)
	public static class AuthorsRouterConfig {

	}

	@Autowired
	@Qualifier("authorsRoute")
	private RouterFunction<ServerResponse> route;

	@MockBean
	private FieldValidator validator;

	@MockBean
	private AuthorsRepository authorsRepository;

	private WebTestClient testClient;

	private Gson gson;

	private Author author;

	@BeforeEach
	void setUp() {
		this.testClient = WebTestClient.bindToRouterFunction(route).build();
		this.gson = new GsonBuilder().create();
		this.author = new Author.Builder()
				.id(AUTHOR_ID)
				.name(AUTHOR_NAME)
				.surname(AUTHOR_SURNAME)
				.build();
	}

	@Test
	@DisplayName("should save author by POST HTTP request")
	public void shouldSaveAuthor() {
		var authorCandidate = new Author.Builder()
				.name(AUTHOR_NAME)
				.surname(AUTHOR_SURNAME)
				.build();

		given(validator.validate(any()))
				.willReturn(new BeanPropertyBindingResult(author, author.getClass().getName()));
		given(authorsRepository.save(any()))
				.willReturn(Mono.just(author));

		testClient.post()
				.uri(AUTHOR_DOMAIN_URL)
				.bodyValue(authorCandidate)
				.accept(APPLICATION_JSON)
				.exchange()
				.expectStatus()
				.isCreated()
				.expectHeader()
				.contentType(APPLICATION_JSON)
				.expectBody()
				.json(gson.toJson(new AuthorDto(author)));
	}

	@Test
	@DisplayName("should return all authors by GET HTTP request")
	public void shouldReturnAllAuthors() {
		var authors = List.of(new AuthorDto(author));

		given(authorsRepository.findAll()).willReturn(Flux.just(author));

		testClient.get()
				.uri(AUTHOR_DOMAIN_URL)
				.exchange()
				.expectStatus()
				.isOk()
				.expectHeader()
				.contentType(APPLICATION_JSON)
				.expectBody()
				.json(gson.toJson(authors));
	}

	@Test
	@DisplayName("should return author by id by GET HTTP request with path variable")
	public void shouldReturnAuthorById() {
		given(authorsRepository.findById(AUTHOR_ID)).willReturn(Mono.just(author));

		testClient.get()
				.uri(uriBuilder -> uriBuilder.path(AUTHOR_BY_ID_URL).build(AUTHOR_ID))
				.exchange()
				.expectStatus()
				.isOk()
				.expectHeader()
				.contentType(APPLICATION_JSON)
				.expectBody()
				.json(gson.toJson(new AuthorDto(author)));
	}

	@Test
	@DisplayName("should update author by PUT HTTP request")
	public void shouldUpdateAuthor() {
		given(validator.validate(any()))
				.willReturn(new BeanPropertyBindingResult(author, author.getClass().getName()));
		given(authorsRepository.save(any())).willReturn(Mono.just(author));

		testClient.put()
				.uri(AUTHOR_DOMAIN_URL)
				.accept(APPLICATION_JSON)
				.bodyValue(author)
				.exchange()
				.expectStatus()
				.isNoContent();
	}

	@Test
	@DisplayName("should delete author by DELETE HTTP request")
	public void shouldDeleteAuthor() {
		given(authorsRepository.deleteById(AUTHOR_ID)).willReturn(Mono.empty());

		testClient.delete()
				.uri(uriBuilder -> uriBuilder.path(AUTHOR_BY_ID_URL).build(AUTHOR_ID))
				.exchange()
				.expectStatus()
				.isNoContent();
	}
}