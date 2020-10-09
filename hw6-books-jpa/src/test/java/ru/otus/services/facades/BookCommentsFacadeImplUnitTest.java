package ru.otus.services.facades;

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
import ru.otus.domain.Book;
import ru.otus.domain.Comment;
import ru.otus.services.BookCommentsService;
import ru.otus.services.BooksService;
import ru.otus.services.CommentsService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;

@SpringBootTest
@DisplayName("facade for work with book comments")
class BookCommentsFacadeImplUnitTest {

	private static final long BOOK_ID = 1L;

	@Configuration
	@Import(BookCommentsFacadeImpl.class)
	@EnableConfigurationProperties(AppProperties.class)
	public static class BookCommentsFacadeConfig{

	}

	@MockBean
	private CommentsService commentsService;

	@MockBean
	private BooksService booksService;

	@MockBean
	private BookCommentsService bookCommentsService;

	@Autowired
	private BookCommentsFacade bookCommentsFacade;

	private InOrder inOrder;

	@BeforeEach
	void setUp() {
		this.inOrder = inOrder(bookCommentsService, commentsService, booksService);
	}

	@Test
	@DisplayName("should call services for save book comment")
	public void shouldCallServicesForSaveBookComment() {
		var comment = new Comment();
		given(booksService.getById(BOOK_ID)).willReturn(Optional.of(new Book()));
		bookCommentsFacade.save(BOOK_ID, comment);
		inOrder.verify(booksService, times(1)).getById(BOOK_ID);
		inOrder.verify(commentsService, times(1)).save(comment);
		inOrder.verify(bookCommentsService, times(1)).save(any());
	}
}