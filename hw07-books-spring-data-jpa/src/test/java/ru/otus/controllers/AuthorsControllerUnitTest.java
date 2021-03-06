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
import ru.otus.services.AuthorsService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;

@SpringBootTest
@DisplayName("controller for work with authors")
class AuthorsControllerUnitTest {

	private static final long AUTHOR_ID = 1L;
	private static final String SURNAME = "surname";
	private static final String NAME = "name";

	@Configuration
	@Import({AuthorsController.class})
	@EnableConfigurationProperties(AppProperties.class)
	public static class AuthorsControllerConfig {
	}

	@MockBean
	private AuthorsService authorsService;

	@Autowired
	private AuthorsController authorsController;
	private InOrder inOrder;

	@BeforeEach
	void setUp() {
		this.inOrder = inOrder(authorsService);
	}

	@Test
	@DisplayName("should call service for create author")
	public void shouldCallServiceForCreateAuthor() {
		authorsController.create(NAME, SURNAME);
		inOrder.verify(authorsService, times(1)).save(any());
	}

	@Test
	@DisplayName("should call service for update author")
	public void shouldCallServiceForUpdateAuthor() {
		authorsController.update(AUTHOR_ID, NAME, SURNAME);
		inOrder.verify(authorsService, times(1)).save(any());
	}

	@Test
	@DisplayName("should call service for getting author")
	public void shouldCallServiceForGettingAuthor() {
		authorsController.getById(AUTHOR_ID);
		inOrder.verify(authorsService, times(1)).getById(AUTHOR_ID);
	}

	@Test
	@DisplayName("should call service for getting all authors")
	public void shouldCallServiceForGettingAllAuthors() {
		authorsController.getAll();
		inOrder.verify(authorsService, times(1)).getAll();
	}

	@Test
	@DisplayName("should call service for remove author")
	public void shouldCallServiceForRemoveAuthor() {
		authorsController.remove(AUTHOR_ID);
		inOrder.verify(authorsService, times(1)).deleteById(AUTHOR_ID);
	}
}