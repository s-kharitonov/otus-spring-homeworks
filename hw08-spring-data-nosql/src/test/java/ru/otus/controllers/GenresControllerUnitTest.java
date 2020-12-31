package ru.otus.controllers;

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
import ru.otus.services.GenresService;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;

@SpringBootTest
@DisplayName("controller for genres")
class GenresControllerUnitTest {

	private static final String GENRE_ID = "1";
	public static final String NAME = "name";

	@Import(GenresController.class)
	@Configuration
	@EnableConfigurationProperties
	public static class GenresControllerConfig {

	}

	@MockBean
	private GenresService genresService;

	@Autowired
	private GenresController genresController;

	private InOrder inOrder;

	@BeforeEach
	void setUp() {
		this.inOrder = inOrder(genresService);
	}

	@Test
	@DisplayName("should call service for create genre")
	public void shouldCallServiceForCreateGenre() {
		assertDoesNotThrow(() -> genresController.create(NAME));
		inOrder.verify(genresService, times(1)).save(any());
	}

	@Test
	@DisplayName("should call service for update genre")
	public void shouldCallServiceForUpdateGenre() {
		assertDoesNotThrow(() -> genresController.update(GENRE_ID, NAME));
		inOrder.verify(genresService, times(1)).save(any());
	}

	@Test
	@DisplayName("should call service for getting genre")
	public void shouldCallServiceForGettingGenre() {
		assertDoesNotThrow(() -> genresController.getById(GENRE_ID));
		inOrder.verify(genresService, times(1)).getById(GENRE_ID);
	}

	@Test
	@DisplayName("should call service for getting all genres")
	public void shouldCallServiceForGettingAllGenres() {
		assertDoesNotThrow(() -> genresController.getAll());
		inOrder.verify(genresService, times(1)).getAll();
	}

	@Test
	@DisplayName("should call service for delete genre")
	public void shouldCallServiceForDeleteGenre() {
		assertDoesNotThrow(() -> genresController.delete(GENRE_ID));
		inOrder.verify(genresService, times(1)).deleteById(GENRE_ID);
	}
}