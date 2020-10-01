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
import ru.otus.services.BooksService;
import ru.otus.services.LocalizationService;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;

@SpringBootTest
@DisplayName("controller for work with books")
class BooksControllerUnitTest {

	private static final long BOOK_ID = 1L;
	private static final String EMPTY_APP_MESSAGE = "";

	@Configuration
	@Import({BooksController.class})
	@EnableConfigurationProperties(AppProperties.class)
	public static class BooksControllerConfig {
	}

	@MockBean
	private BooksService booksService;

	@MockBean
	private LocalizationService localizationService;

	@Autowired
	private BooksController booksController;
	private InOrder inOrder;

	@BeforeEach
	void setUp() {
		this.inOrder = inOrder(booksService);
	}

	@Test
	@DisplayName("should call service for getting book")
	public void shouldCallServiceForGettingBook() {
		given(localizationService.localizeMessage(Constants.BOOK_NOT_FOUND_MSG_KEY, BOOK_ID))
				.willReturn(EMPTY_APP_MESSAGE);
		booksController.getBookById(BOOK_ID);
		inOrder.verify(booksService, times(1)).getBookById(BOOK_ID);
	}

	@Test
	@DisplayName("should call service for getting all books")
	public void shouldCallServiceForGettingAllBooks() {
		booksController.getAllBooks();
		inOrder.verify(booksService, times(1)).getAllBooks();
	}

	@Test
	@DisplayName("should call service for remove book")
	public void shouldCallServiceForRemoveBook() {
		given(booksService.removeBook(BOOK_ID)).willReturn(true);
		given(localizationService.localizeMessage(Constants.BOOK_SUCCESSFUL_REMOVED_MSG_KEY, BOOK_ID))
				.willReturn(EMPTY_APP_MESSAGE);
		booksController.removeBook(BOOK_ID);
		inOrder.verify(booksService, times(1)).removeBook(BOOK_ID);
	}
}