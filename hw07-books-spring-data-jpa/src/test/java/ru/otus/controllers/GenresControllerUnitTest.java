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
import ru.otus.configs.AppProperties;
import ru.otus.services.GenresService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;

@SpringBootTest
@DisplayName("controller for work with genres")
class GenresControllerUnitTest {

	private static final long GENRE_ID = 1L;
	public static final String NAME = "name";

	@Configuration
	@Import({GenresController.class})
	@EnableConfigurationProperties(AppProperties.class)
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
		genresController.create(NAME);
		inOrder.verify(genresService, times(1)).save(any());
	}

	@Test
	@DisplayName("should call service for update genre")
	public void shouldCallServiceForUpdateGenre() {
		genresController.update(GENRE_ID, NAME);
		inOrder.verify(genresService, times(1)).save(any());
	}

	@Test
	@DisplayName("should call service for getting genre")
	public void shouldCallServiceForGettingGenre() {
		genresController.getById(GENRE_ID);
		inOrder.verify(genresService, times(1)).getById(GENRE_ID);
	}

	@Test
	@DisplayName("should call service for getting all genres")
	public void shouldCallServiceForGettingAllGenres() {
		genresController.getAll();
		inOrder.verify(genresService, times(1)).getAll();
	}

	@Test
	@DisplayName("should call service for remove genre")
	public void shouldCallServiceForRemoveGenre() {
		genresController.remove(GENRE_ID);
		inOrder.verify(genresService, times(1)).deleteById(GENRE_ID);
	}
}