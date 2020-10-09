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
import ru.otus.services.CommentsService;
import ru.otus.services.LocalizationService;
import ru.otus.services.facades.BookCommentsFacade;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;

@SpringBootTest
@DisplayName("controller for work with comments")
class CommentsControllerUnitTest {

	private static final long COMMENT_ID = 1L;
	private static final String EMPTY_APP_MESSAGE = "";
	private static final String COMMENT_TEXT = "hello world!";
	private static final long BOOK_ID = 1L;

	@Configuration
	@Import(CommentsController.class)
	@EnableConfigurationProperties(AppProperties.class)
	public static class CommentsControllerConfig {

	}

	@MockBean
	private BookCommentsFacade bookCommentsFacade;

	@MockBean
	private CommentsService commentsService;

	@MockBean
	private LocalizationService localizationService;

	@Autowired
	private CommentsController commentsController;
	private InOrder inOrder;

	@BeforeEach
	void setUp() {
		this.inOrder = inOrder(commentsService, bookCommentsFacade);
	}

	@Test
	@DisplayName("should call service for create comment")
	public void shouldCallServiceForCreateComment() {
		commentsController.create(COMMENT_TEXT);
		inOrder.verify(commentsService, times(1)).save(any());
	}

	@Test
	@DisplayName("should call service for update comment")
	public void shouldCallServiceForUpdateComment() {
		commentsController.update(COMMENT_ID, COMMENT_TEXT);
		inOrder.verify(commentsService, times(1)).save(any());
	}

	@Test
	@DisplayName("should call service for create book comment")
	public void shouldCallServiceForCreateBookComment() {
		commentsController.createBookComment(BOOK_ID, COMMENT_TEXT);
		inOrder.verify(bookCommentsFacade, times(1)).save(anyLong(), any());
	}

	@Test
	@DisplayName("should call service for getting comment by id")
	public void shouldCallServiceForGettingCommentById() {
		commentsController.getById(COMMENT_ID);
		inOrder.verify(commentsService, times(1)).getById(COMMENT_ID);
	}

	@Test
	@DisplayName("should call service for getting all comments")
	public void shouldCallServiceForGettingAllComments() {
		commentsController.getAll();
		inOrder.verify(commentsService, times(1)).getAll();
	}

	@Test
	@DisplayName("should call service for remove service")
	public void shouldCallServiceForRemoveComment() {
		given(commentsService.removeById(COMMENT_ID)).willReturn(true);
		given(localizationService.localizeMessage(Constants.COMMENT_SUCCESSFUL_REMOVED_MSG_KEY, COMMENT_ID))
				.willReturn(EMPTY_APP_MESSAGE);

		commentsController.removeById(COMMENT_ID);
		inOrder.verify(commentsService, times(1)).removeById(COMMENT_ID);
	}
}