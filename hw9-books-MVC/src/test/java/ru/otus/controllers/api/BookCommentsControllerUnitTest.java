package ru.otus.controllers.api;

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
import ru.otus.services.facades.BookCommentsFacade;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;

@SpringBootTest
@DisplayName("controller for work with comments")
class BookCommentsControllerUnitTest {

	private static final long COMMENT_ID = 1L;
	private static final long BOOK_ID = 1L;
	private static final String COMMENT_TEXT = "hello world!";

	@Configuration
	@Import(BookCommentsController.class)
	@EnableConfigurationProperties(AppProperties.class)
	public static class CommentsControllerConfig {

	}

	@MockBean
	private BookCommentsFacade bookCommentsFacade;

	@Autowired
	private BookCommentsController bookCommentsController;
	private InOrder inOrder;

	@BeforeEach
	void setUp() {
		this.inOrder = inOrder(bookCommentsFacade);
	}

	@Test
	@DisplayName("should call service for create comment")
	public void shouldCallServiceForCreateComment() {
		bookCommentsController.create(COMMENT_TEXT, BOOK_ID);
		inOrder.verify(bookCommentsFacade, times(1)).save(any());
	}

	@Test
	@DisplayName("should call service for update comment")
	public void shouldCallServiceForUpdateComment() {
		bookCommentsController.update(COMMENT_ID, COMMENT_TEXT);
		inOrder.verify(bookCommentsFacade, times(1)).save(any());
	}

	@Test
	@DisplayName("should call service for getting comment by id")
	public void shouldCallServiceForGettingCommentById() {
		bookCommentsController.getById(COMMENT_ID);
		inOrder.verify(bookCommentsFacade, times(1)).getById(COMMENT_ID);
	}

	@Test
	@DisplayName("should call service for getting all comments")
	public void shouldCallServiceForGettingAllComments() {
		bookCommentsController.getAll();
		inOrder.verify(bookCommentsFacade, times(1)).getAll();
	}

	@Test
	@DisplayName("should call service for remove service")
	public void shouldCallServiceForRemoveComment() {
		bookCommentsController.removeById(COMMENT_ID);
		inOrder.verify(bookCommentsFacade, times(1)).deleteById(COMMENT_ID);
	}
}