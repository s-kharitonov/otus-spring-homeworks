package ru.otus.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.services.BooksService;
import ru.otus.services.LocalizationService;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;

@SpringBootTest
class BooksControllerUnitTest {

	private static final long BOOK_ID = 1L;

	@MockBean
	private BooksService booksService;

	@MockBean
	private LocalizationService localizationService;

	@Autowired
	private BooksController booksController;
	private InOrder inOrder;

	@BeforeEach
	void setUp() {
		this.inOrder = inOrder(booksService, localizationService);
	}

	@Test
	@DisplayName("should call service for getting book")
	public void shouldCallServiceForGettingBook() {
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
		booksController.removeBook(BOOK_ID);
		inOrder.verify(booksService, times(1)).removeBook(BOOK_ID);
	}
}