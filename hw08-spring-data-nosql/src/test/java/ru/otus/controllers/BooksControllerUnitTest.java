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
import ru.otus.services.facades.BooksFacade;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;

@SpringBootTest
@DisplayName("controller for books")
class BooksControllerUnitTest {

	private static final String BOOK_ID = "1";
	private static final String BOOK_NAME = "name";
	private static final Date PUBLICATION_DATE = new Date();
	private static final int PRINT_LENGTH = 200;
	private static final String AUTHOR_ID = "1";
	private static final String GENRE_ID = "1";

	@Import(BooksController.class)
	@Configuration
	@EnableConfigurationProperties
	public static class BooksControllerConfig {

	}

	@MockBean
	private BooksFacade booksFacade;

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
		assertDoesNotThrow(() -> booksController.create(BOOK_NAME, PUBLICATION_DATE, PRINT_LENGTH, AUTHOR_ID, GENRE_ID));
		inOrder.verify(booksFacade, times(1)).save(any());
	}

	@Test
	@DisplayName("should call service for update book")
	public void shouldCallServiceForUpdateBook() {
		assertDoesNotThrow(() -> booksController.updateBook(BOOK_ID, BOOK_NAME, PUBLICATION_DATE, PRINT_LENGTH, AUTHOR_ID, GENRE_ID));
		inOrder.verify(booksFacade, times(1)).save(any());
	}

	@Test
	@DisplayName("should call service for getting book")
	public void shouldCallServiceForGettingBook() {
		assertDoesNotThrow(() -> booksController.getBookById(BOOK_ID));
		inOrder.verify(booksFacade, times(1)).getById(BOOK_ID);
	}

	@Test
	@DisplayName("should call service for getting all books")
	public void shouldCallServiceForGettingAllBooks() {
		assertDoesNotThrow(() -> booksController.getAllBooks());
		inOrder.verify(booksFacade, times(1)).getAll();
	}

	@Test
	@DisplayName("should call service for delete book")
	public void shouldCallServiceForDeleteBook() {
		assertDoesNotThrow(() -> booksController.deleteBook(BOOK_ID));
		inOrder.verify(booksFacade, times(1)).deleteById(BOOK_ID);
	}
}