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
import ru.otus.domain.Constants;
import ru.otus.services.AuthorsService;
import ru.otus.services.LocalizationService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;

@SpringBootTest
@DisplayName("controller for work with authors")
class AuthorsControllerUnitTest {

	private static final long AUTHOR_ID = 1L;
	private static final String EMPTY_APP_MESSAGE = "";
	private static final String SURNAME = "surname";
	private static final String NAME = "name";

	@Configuration
	@Import({AuthorsController.class})
	@EnableConfigurationProperties(AppProperties.class)
	public static class AuthorsControllerConfig {
	}

	@MockBean
	private AuthorsService authorsService;

	@MockBean
	private LocalizationService localizationService;

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
		given(authorsService.removeById(AUTHOR_ID)).willReturn(true);
		given(localizationService.localizeMessage(Constants.AUTHOR_SUCCESSFUL_REMOVED_MSG_KEY, AUTHOR_ID))
				.willReturn(EMPTY_APP_MESSAGE);
		authorsController.remove(AUTHOR_ID);
		inOrder.verify(authorsService, times(1)).removeById(AUTHOR_ID);
	}
}