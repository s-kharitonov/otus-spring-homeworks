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
import ru.otus.domain.dto.BookCommentDto;
import ru.otus.domain.dto.BookDto;
import ru.otus.services.BookCommentsService;
import ru.otus.services.BooksService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;

@SpringBootTest
@DisplayName("facade for work with book comments")
class BookCommentsFacadeImplUnitTest {

	private static final long BOOK_ID = 1L;
	private static final String COMMENT = "comment";
	private static final long BOOK_COMMENT_ID = 1L;

	@Configuration
	@Import(BookCommentsFacadeImpl.class)
	@EnableConfigurationProperties(AppProperties.class)
	public static class BookCommentsFacadeImplConfig {

	}

	@MockBean
	private BooksService booksService;

	@MockBean
	private BookCommentsService bookCommentsService;

	@Autowired
	private BookCommentsFacade bookCommentsFacade;

	private InOrder inOrder;

	@BeforeEach
	void setUp() {
		this.inOrder = inOrder(booksService, bookCommentsService);
	}

	@Test
	@DisplayName("should call services for save book comment")
	public void shouldCallServicesForSaveBookComment() {
		var book = new BookDto.Builder().id(BOOK_ID).build();
		var bookComment = new BookCommentDto.Builder()
				.text(COMMENT)
				.book(book)
				.build();

		given(booksService.getById(BOOK_ID)).willReturn(Optional.of(new Book()));
		bookCommentsFacade.save(bookComment);

		inOrder.verify(booksService, times(1)).getById(BOOK_ID);
		inOrder.verify(bookCommentsService, times(1)).save(any());
	}

	@Test
	@DisplayName("should call service for getting book comment by Id")
	public void shouldCallServiceForGettingBookCommentById() {
		bookCommentsFacade.getById(BOOK_COMMENT_ID);
		inOrder.verify(bookCommentsService, times(1)).getById(BOOK_COMMENT_ID);
	}

	@Test
	@DisplayName("should call service for getting all book comments")
	public void shouldCallServiceForGettingAllBookComments() {
		bookCommentsFacade.getAll();
		inOrder.verify(bookCommentsService, times(1)).getAll();
	}

	@Test
	@DisplayName("should call service for remove book comment by id")
	public void shouldCallServiceForRemoveBookCommentById() {
		bookCommentsFacade.removeById(BOOK_COMMENT_ID);
		inOrder.verify(bookCommentsService, times(1)).removeById(BOOK_COMMENT_ID);
	}
}