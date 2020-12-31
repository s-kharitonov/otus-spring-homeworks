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
import ru.otus.services.AuthorsService;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;

@SpringBootTest
@DisplayName("controller for authors")
class AuthorsControllerUnitTest {

	private static final String AUTHOR_ID = "1";
	private static final String SURNAME = "surname";
	private static final String NAME = "name";

	@Import(AuthorsController.class)
	@Configuration
	@EnableConfigurationProperties
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
		assertDoesNotThrow(() -> authorsController.create(NAME, SURNAME));
		inOrder.verify(authorsService, times(1)).save(any());
	}

	@Test
	@DisplayName("should call service for update author")
	public void shouldCallServiceForUpdateAuthor() {
		assertDoesNotThrow(() -> authorsController.update(AUTHOR_ID, NAME, SURNAME));
		inOrder.verify(authorsService, times(1)).save(any());
	}

	@Test
	@DisplayName("should call service for getting author")
	public void shouldCallServiceForGettingAuthor() {
		assertDoesNotThrow(() -> authorsController.getById(AUTHOR_ID));
		inOrder.verify(authorsService, times(1)).getById(AUTHOR_ID);
	}

	@Test
	@DisplayName("should call service for getting all authors")
	public void shouldCallServiceForGettingAllAuthors() {
		assertDoesNotThrow(() -> authorsController.getAll());
		inOrder.verify(authorsService, times(1)).getAll();
	}

	@Test
	@DisplayName("should call service for delete author")
	public void shouldCallServiceForDeleteAuthor() {
		assertDoesNotThrow(() -> authorsController.delete(AUTHOR_ID));
		inOrder.verify(authorsService, times(1)).deleteById(AUTHOR_ID);
	}
}