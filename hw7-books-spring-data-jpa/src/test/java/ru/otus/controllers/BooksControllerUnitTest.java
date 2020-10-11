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
import ru.otus.services.LocalizationService;
import ru.otus.services.facades.BooksFacade;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;

@SpringBootTest
@DisplayName("controller for work with books")
class BooksControllerUnitTest {

	private static final long BOOK_ID = 1L;
	private static final String EMPTY_APP_MESSAGE = "";
	private static final String BOOK_NAME = "name";
	private static final Date PUBLICATION_DATE = new Date();
	private static final int PRINT_LENGTH = 200;
	private static final long AUTHOR_ID = 1L;
	private static final long GENRE_ID = 1L;

	@Configuration
	@Import({BooksController.class})
	@EnableConfigurationProperties(AppProperties.class)
	public static class BooksControllerConfig {
	}

	@MockBean
	private BooksFacade booksFacade;

	@MockBean
	private LocalizationService localizationService;

	@Autowired
	private BooksController booksController;
	private InOrder inOrder;

	@BeforeEach
	void setUp() {
		this.inOrder = inOrder(booksFacade);
	}

	@Test
	@DisplayName("should call service for create book")
	public void shouldCallServiceForCreateBook() {
		booksController.createBook(BOOK_NAME, PUBLICATION_DATE, PRINT_LENGTH, AUTHOR_ID, GENRE_ID);
		inOrder.verify(booksFacade, times(1)).save(any());
	}

	@Test
	@DisplayName("should call service for update book")
	public void shouldCallServiceForUpdateBook() {
		booksController.updateBook(BOOK_ID, BOOK_NAME, PUBLICATION_DATE, PRINT_LENGTH, AUTHOR_ID, GENRE_ID);
		inOrder.verify(booksFacade, times(1)).save(any());
	}

	@Test
	@DisplayName("should call service for getting book")
	public void shouldCallServiceForGettingBook() {
		given(localizationService.localizeMessage(Constants.BOOK_NOT_FOUND_MSG_KEY, BOOK_ID))
				.willReturn(EMPTY_APP_MESSAGE);
		booksController.getBookById(BOOK_ID);
		inOrder.verify(booksFacade, times(1)).getById(BOOK_ID);
	}

	@Test
	@DisplayName("should call service for getting all books")
	public void shouldCallServiceForGettingAllBooks() {
		booksController.getAllBooks();
		inOrder.verify(booksFacade, times(1)).getAll();
	}

	@Test
	@DisplayName("should call service for remove book")
	public void shouldCallServiceForRemoveBook() {
		given(booksFacade.removeById(BOOK_ID)).willReturn(true);
		given(localizationService.localizeMessage(Constants.BOOK_SUCCESSFUL_REMOVED_MSG_KEY, BOOK_ID))
				.willReturn(EMPTY_APP_MESSAGE);
		booksController.removeBook(BOOK_ID);
		inOrder.verify(booksFacade, times(1)).removeById(BOOK_ID);
	}
}