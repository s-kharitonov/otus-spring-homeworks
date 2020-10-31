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
import ru.otus.domain.Author;
import ru.otus.domain.dto.AuthorDto;
import ru.otus.services.AuthorsService;

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

@WebMvcTest(AuthorsRestController.class)
@DisplayName("controller for work with authors")
class AuthorsRestControllerUnitTest {

	private static final long AUTHOR_ID = 1L;
	private static final String AUTHOR_SURNAME = "surname";
	private static final String AUTHOR_NAME = "name";
	private static final String AUTHOR_DOMAIN_URL = "/author/";
	private static final String AUTHOR_BY_ID_URL = AUTHOR_DOMAIN_URL + "{id}";

	@Autowired
	private MockMvc mvc;

	@MockBean
	private AuthorsService authorsService;

	private InOrder inOrder;
	private Gson gson;

	@BeforeEach
	void setUp() {
		this.inOrder = inOrder(authorsService);
		this.gson = new GsonBuilder().create();
	}

	@Test
	@DisplayName("should create author")
	public void shouldCreateAuthor() throws Exception {
		var authorForCreate = new Author.Builder()
				.name(AUTHOR_NAME)
				.surname(AUTHOR_SURNAME)
				.build();
		var expectedAuthor = new Author.Builder()
				.id(AUTHOR_ID)
				.name(AUTHOR_NAME)
				.surname(AUTHOR_SURNAME)
				.build();
		var requestBuilder = post(AUTHOR_DOMAIN_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(gson.toJson(authorForCreate));

		given(authorsService.save(any())).willReturn(expectedAuthor);

		mvc.perform(requestBuilder)
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(gson.toJson(new AuthorDto(expectedAuthor))));

		inOrder.verify(authorsService, times(1)).save(any());
	}

	@Test
	@DisplayName("should getting author by id")
	public void shouldGettingAuthorById() throws Exception {
		var expectedAuthor = new Author.Builder()
				.id(AUTHOR_ID)
				.name(AUTHOR_NAME)
				.surname(AUTHOR_SURNAME)
				.build();
		var requestBuilder = get(AUTHOR_BY_ID_URL, AUTHOR_ID)
				.contentType(MediaType.APPLICATION_JSON);

		given(authorsService.getById(AUTHOR_ID)).willReturn(Optional.of(expectedAuthor));

		mvc.perform(requestBuilder)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(gson.toJson(new AuthorDto(expectedAuthor))));

		inOrder.verify(authorsService, times(1)).getById(AUTHOR_ID);
	}

	@Test
	@DisplayName("should return not found status code when author by id is not found")
	public void shouldReturnNotFoundStatusCodeWhenAuthorByIdIsNotFound() throws Exception {
		var requestBuilder = get(AUTHOR_BY_ID_URL, AUTHOR_ID)
				.contentType(MediaType.APPLICATION_JSON);

		given(authorsService.getById(AUTHOR_ID)).willReturn(Optional.empty());

		mvc.perform(requestBuilder)
				.andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}

	@Test
	@DisplayName("should getting all authors")
	public void shouldGettingAllAuthors() throws Exception {
		var author = new Author.Builder()
				.id(AUTHOR_ID)
				.name(AUTHOR_NAME)
				.surname(AUTHOR_SURNAME)
				.build();
		var expectedAuthors = List.of(author);
		var expectedAuthorsDto = expectedAuthors.stream()
				.map(AuthorDto::new)
				.collect(Collectors.toList());
		var requestBuilder = get(AUTHOR_DOMAIN_URL)
				.contentType(MediaType.APPLICATION_JSON);

		given(authorsService.getAll()).willReturn(expectedAuthors);

		mvc.perform(requestBuilder)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(gson.toJson(expectedAuthorsDto)));

		inOrder.verify(authorsService, times(1)).getAll();
	}

	@Test
	@DisplayName("should delete author")
	public void shouldDeleteAuthor() throws Exception {
		var requestBuilder = delete(AUTHOR_BY_ID_URL, AUTHOR_ID)
				.contentType(MediaType.APPLICATION_JSON);

		mvc.perform(requestBuilder).andExpect(status().isNoContent());

		inOrder.verify(authorsService, times(1)).deleteById(AUTHOR_ID);
	}

	@Test
	@DisplayName("should update author")
	public void shouldUpdateAuthor() throws Exception {
		var authorForUpdate = new Author.Builder()
				.id(AUTHOR_ID)
				.name(AUTHOR_NAME)
				.surname(AUTHOR_SURNAME)
				.build();
		var requestBuilder = put(AUTHOR_DOMAIN_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(gson.toJson(authorForUpdate));

		mvc.perform(requestBuilder).andExpect(status().isNoContent());

		inOrder.verify(authorsService, times(1)).save(any());
	}
}