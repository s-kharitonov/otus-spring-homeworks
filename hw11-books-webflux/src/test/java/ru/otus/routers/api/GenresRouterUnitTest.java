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
import ru.otus.domain.Genre;
import ru.otus.repositories.GenresRepository;
import ru.otus.validators.FieldValidator;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest
@DisplayName("route for genres")
class GenresRouterUnitTest {

	private static final String GENRE_ID = "1";
	private static final String GENRE_NAME = "Horror";
	private static final String GENRE_DOMAIN_URL = "/api/genre/";
	private static final String GENRE_BY_ID_URL = GENRE_DOMAIN_URL + "{id}";

	@Configuration
	@Import(GenresRouter.class)
	public static class GenreRouterConfig {

	}

	@Autowired
	@Qualifier("genresRoute")
	private RouterFunction<ServerResponse> route;

	@MockBean
	private GenresRepository genresRepository;

	@MockBean
	private FieldValidator validator;

	private WebTestClient testClient;

	private Gson gson;

	private Genre genre;

	@BeforeEach
	void setUp() {
		this.testClient = WebTestClient.bindToRouterFunction(route).build();
		this.gson = new GsonBuilder().create();
		this.genre = new Genre.Builder()
				.id(GENRE_ID)
				.name(GENRE_NAME)
				.build();
	}

	@Test
	@DisplayName("should save genre by POST HTTP request")
	public void shouldSaveGenre() {
		var candidate = new Genre.Builder()
				.name(GENRE_NAME)
				.build();

		given(validator.validate(any())).willReturn(new BeanPropertyBindingResult(candidate, candidate.getClass().getName()));
		given(genresRepository.save(any())).willReturn(Mono.just(genre));

		testClient.post()
				.uri(GENRE_DOMAIN_URL)
				.accept(APPLICATION_JSON)
				.bodyValue(candidate)
				.exchange()
				.expectStatus()
				.isCreated()
				.expectHeader()
				.contentType(APPLICATION_JSON)
				.expectBody()
				.json(gson.toJson(genre));
	}

	@Test
	@DisplayName("should return all genres by GET HTTP request")
	public void shouldReturnAllGenres() {
		var genres = List.of(genre);

		given(genresRepository.findAll()).willReturn(Flux.just(genre));

		testClient.get()
				.uri(GENRE_DOMAIN_URL)
				.exchange()
				.expectStatus()
				.isOk()
				.expectHeader()
				.contentType(APPLICATION_JSON)
				.expectBody()
				.json(gson.toJson(genres));
	}

	@Test
	@DisplayName("should return genre by id by GET HTTP request")
	public void shouldReturnGenreById() {
		given(genresRepository.findById(GENRE_ID)).willReturn(Mono.just(genre));

		testClient.get()
				.uri(uriBuilder -> uriBuilder.path(GENRE_BY_ID_URL).build(GENRE_ID))
				.exchange()
				.expectStatus()
				.isOk()
				.expectHeader()
				.contentType(APPLICATION_JSON)
				.expectBody()
				.json(gson.toJson(genre));
	}

	@Test
	@DisplayName("should update genre by PUT HTTP request")
	public void shouldUpdateGenre() {
		given(validator.validate(any())).willReturn(new BeanPropertyBindingResult(genre, genre.getClass().getName()));
		given(genresRepository.save(any())).willReturn(Mono.just(genre));

		testClient.put()
				.uri(GENRE_DOMAIN_URL)
				.accept(APPLICATION_JSON)
				.bodyValue(genre)
				.exchange()
				.expectStatus()
				.isNoContent();
	}

	@Test
	@DisplayName("should delete genre by DELETE HTTP request")
	public void shouldDeleteGenre() {
		given(genresRepository.deleteById(GENRE_ID)).willReturn(Mono.empty());

		testClient.delete()
				.uri(uriBuilder -> uriBuilder.path(GENRE_BY_ID_URL).build(GENRE_ID))
				.exchange()
				.expectStatus()
				.isNoContent();
	}
}