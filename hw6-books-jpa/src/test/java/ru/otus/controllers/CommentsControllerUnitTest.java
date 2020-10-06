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

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;

@SpringBootTest
@DisplayName("controller for work with comments")
class CommentsControllerUnitTest {

	private static final long COMMENT_ID = 1L;
	private static final String EMPTY_APP_MESSAGE = "";

	@Configuration
	@Import(CommentsController.class)
	@EnableConfigurationProperties(AppProperties.class)
	public static class CommentsControllerConfig {

	}

	@MockBean
	private CommentsService commentsService;

	@MockBean
	private LocalizationService localizationService;

	@Autowired
	private CommentsController commentsController;
	private InOrder inOrder;

	@BeforeEach
	void setUp() {
		this.inOrder = inOrder(commentsService);
	}

	@Test
	@DisplayName("should call CommentsService for getting comment by id")
	public void shouldCallServiceForGettingCommentById() {
		commentsController.getById(COMMENT_ID);
		inOrder.verify(commentsService, times(1)).getById(COMMENT_ID);
	}

	@Test
	@DisplayName("should call CommentsService for getting all comments")
	public void shouldCallServiceForGettingAllComments() {
		commentsController.getAll();
		inOrder.verify(commentsService, times(1)).getAll();
	}

	@Test
	@DisplayName("should call CommentsService for remove service")
	public void shouldCallServiceForRemoveComment() {
		given(commentsService.removeById(COMMENT_ID)).willReturn(true);
		given(localizationService.localizeMessage(Constants.COMMENT_SUCCESSFUL_REMOVED_MSG_KEY, COMMENT_ID))
				.willReturn(EMPTY_APP_MESSAGE);

		commentsController.removeById(COMMENT_ID);
		inOrder.verify(commentsService, times(1)).removeById(COMMENT_ID);
	}
}