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
import ru.otus.services.facades.BookCommentsFacade;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;

@SpringBootTest
@DisplayName("controller for book comments")
class BookCommentsControllerUnitTest {

	private static final String COMMENT_ID = "1";
	private static final String BOOK_ID = "1";
	private static final String COMMENT_TEXT = "hello world!";

	@Import(BookCommentsController.class)
	@Configuration
	@EnableConfigurationProperties
	public static class BookCommentsConfig {

	}

	@MockBean
	private BookCommentsFacade bookCommentsFacade;

	@Autowired
	private BookCommentsController commentsController;

	private InOrder inOrder;

	@BeforeEach
	void setUp() {
		this.inOrder = inOrder(bookCommentsFacade);
	}

	@Test
	@DisplayName("should call service for create comment")
	public void shouldCallServiceForCreateComment() {
		assertDoesNotThrow(() -> commentsController.create(COMMENT_TEXT, BOOK_ID));
		inOrder.verify(bookCommentsFacade, times(1)).save(any());
	}

	@Test
	@DisplayName("should call service for update comment")
	public void shouldCallServiceForUpdateComment() {
		assertDoesNotThrow(() -> commentsController.update(COMMENT_ID, COMMENT_TEXT, BOOK_ID));
		inOrder.verify(bookCommentsFacade, times(1)).save(any());
	}

	@Test
	@DisplayName("should call service for getting comment by id")
	public void shouldCallServiceForGettingCommentById() {
		assertDoesNotThrow(() -> commentsController.getById(COMMENT_ID));
		inOrder.verify(bookCommentsFacade, times(1)).getById(COMMENT_ID);
	}

	@Test
	@DisplayName("should call service for getting all comments")
	public void shouldCallServiceForGettingAllComments() {
		assertDoesNotThrow(() -> commentsController.getAll());
		inOrder.verify(bookCommentsFacade, times(1)).getAll();
	}

	@Test
	@DisplayName("should call service for delete service")
	public void shouldCallServiceForDeleteComment() {
		assertDoesNotThrow(() -> commentsController.deleteById(COMMENT_ID));
		inOrder.verify(bookCommentsFacade, times(1)).deleteById(COMMENT_ID);
	}
}