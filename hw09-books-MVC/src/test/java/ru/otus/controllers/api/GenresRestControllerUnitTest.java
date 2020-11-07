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
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.domain.Genre;
import ru.otus.domain.dto.GenreDto;
import ru.otus.services.GenresService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GenresRestController.class)
@DisplayName("controller for work with genres")
class GenresRestControllerUnitTest {

	private static final long GENRE_ID = 1L;
	private static final String GENRE_NAME = "Horror";
	private static final String GENRE_DOMAIN_URL = "/api/genre/";
	private static final String GENRE_BY_ID_URL = GENRE_DOMAIN_URL + "{id}";

	@Autowired
	private MockMvc mvc;

	@MockBean
	private GenresService genresService;

	private InOrder inOrder;
	private Gson gson;

	@BeforeEach
	void setUp() {
		this.inOrder = inOrder(genresService);
		this.gson = new GsonBuilder().create();
	}

	@Test
	@DisplayName("should create genre")
	public void shouldCreateGenre() throws Exception {
		var genreForCreate = new Genre.Builder()
				.name(GENRE_NAME)
				.build();
		var expectedGenre = new Genre.Builder()
				.id(GENRE_ID)
				.name(GENRE_NAME)
				.build();
		var requestBuilder = post(GENRE_DOMAIN_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(gson.toJson(genreForCreate));

		given(genresService.save(any())).willReturn(expectedGenre);

		mvc.perform(requestBuilder)
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(gson.toJson(new GenreDto(expectedGenre))));

		inOrder.verify(genresService, times(1)).save(any());
	}

	@Test
	@DisplayName("should getting genre by id")
	public void shouldGettingGenreById() throws Exception {
		var expectedGenre = new Genre.Builder()
				.id(GENRE_ID)
				.name(GENRE_NAME)
				.build();
		var requestBuilder = get(GENRE_BY_ID_URL, GENRE_ID)
				.contentType(MediaType.APPLICATION_JSON);

		given(genresService.getById(GENRE_ID)).willReturn(Optional.of(expectedGenre));

		mvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(gson.toJson(new GenreDto(expectedGenre))));

		inOrder.verify(genresService, times(1)).getById(GENRE_ID);
	}

	@Test
	@DisplayName("should return not found status code when genre is not found")
	public void shouldReturnNotFoundStatusCodeWhenGenreIsNotFound() throws Exception {
		var requestBuilder = get(GENRE_BY_ID_URL, GENRE_ID)
				.contentType(MediaType.APPLICATION_JSON);

		given(genresService.getById(GENRE_ID)).willReturn(Optional.empty());

		mvc.perform(requestBuilder).andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}

	@Test
	@DisplayName("should getting all genres")
	public void shouldGettingAllGenres() throws Exception {
		var genre = new Genre.Builder()
				.id(GENRE_ID)
				.name(GENRE_NAME)
				.build();
		var expectedGenres = List.of(genre);
		var expectedGenresDto = expectedGenres.stream()
				.map(GenreDto::new)
				.collect(Collectors.toList());
		var requestBuilder = get(GENRE_DOMAIN_URL)
				.contentType(MediaType.APPLICATION_JSON);

		given(genresService.getAll()).willReturn(expectedGenres);

		mvc.perform(requestBuilder)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(gson.toJson(expectedGenresDto)));

		inOrder.verify(genresService, times(1)).getAll();
	}

	@Test
	@DisplayName("should return empty list when genres is not found")
	public void shouldReturnEmptyListWhenGenresIsNotFound() throws Exception {
		List<Genre> expectedGenres = Collections.emptyList();
		var requestBuilder = get(GENRE_DOMAIN_URL)
				.contentType(MediaType.APPLICATION_JSON);

		given(genresService.getAll()).willReturn(expectedGenres);

		mvc.perform(requestBuilder)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(gson.toJson(expectedGenres)));

		inOrder.verify(genresService, times(1)).getAll();
	}

	@Test
	@DisplayName("should delete genre by id")
	public void shouldDeleteGenreById() throws Exception {
		var requestBuilder = delete(GENRE_BY_ID_URL, GENRE_ID)
				.contentType(MediaType.APPLICATION_JSON);

		mvc.perform(requestBuilder)
				.andExpect(status().isNoContent());

		inOrder.verify(genresService, times(1)).deleteById(GENRE_ID);
	}

	@Test
	@DisplayName("should update genre")
	public void shouldUpdateGenre() throws Exception {
		var genreForUpdate = new Genre.Builder()
				.id(GENRE_ID)
				.name(GENRE_NAME)
				.build();
		var requestBuilder = put(GENRE_DOMAIN_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(gson.toJson(genreForUpdate));

		mvc.perform(requestBuilder)
				.andExpect(status().isNoContent());

		inOrder.verify(genresService, times(1)).save(any());
	}
}