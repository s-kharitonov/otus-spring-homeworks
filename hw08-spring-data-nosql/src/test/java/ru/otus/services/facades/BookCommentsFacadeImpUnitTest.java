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
import ru.otus.domain.Author;
import ru.otus.domain.Book;
import ru.otus.domain.BookCommentCandidate;
import ru.otus.domain.Genre;
import ru.otus.services.BookCommentsService;
import ru.otus.services.BooksService;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;

@SpringBootTest
@DisplayName("facade for book comments")
class BookCommentsFacadeImpUnitTest {
	private static final String COMMENT = "perfect book!";
	private static final String COMMENT_ID = "1";
	private static final String GENRE_NAME = "Horror";
	private static final String GENRE_ID = "1";
	private static final String AUTHOR_SURNAME = "kharitonov";
	private static final String AUTHOR_NAME = "sergey";
	private static final String AUTHOR_ID = "1";
	private static final String BOOK_NAME = "Harry Potter";
	private static final Date BOOK_PUBLICATION_DATE = new Date();
	private static final int BOOK_PRINT_LENGTH = 255;
	private static final String BOOK_ID = "1";

	@Configuration
	@Import(BookCommentsFacadeImp.class)
	@EnableConfigurationProperties
	public static class BookCommentsFacadeImplConfig {

	}

	@MockBean
	private BooksService booksService;

	@MockBean
	private BookCommentsService bookCommentsService;

	@Autowired
	private BookCommentsFacade bookCommentsFacade;

	private InOrder inOrder;

	private Book book;

	@BeforeEach
	void setUp() {
		var genre = new Genre.Builder()
				.id(GENRE_ID)
				.name(GENRE_NAME)
				.build();
		var author = new Author.Builder()
				.id(AUTHOR_ID)
				.name(AUTHOR_NAME)
				.surname(AUTHOR_SURNAME)
				.build();
		this.book = new Book.Builder()
				.id(BOOK_ID)
				.name(BOOK_NAME)
				.publicationDate(BOOK_PUBLICATION_DATE)
				.printLength(BOOK_PRINT_LENGTH)
				.author(author)
				.genre(genre)
				.build();
		this.inOrder = inOrder(booksService, bookCommentsService);
	}

	@Test
	@DisplayName("should call services for save book comment")
	public void shouldCallServicesForSaveBookComment() {
		var commentCandidate = new BookCommentCandidate.Builder()
				.commentId(COMMENT_ID)
				.text(COMMENT)
				.bookId(BOOK_ID)
				.build();

		given(booksService.getById(BOOK_ID)).willReturn(Optional.of(book));
		assertDoesNotThrow(() -> bookCommentsFacade.save(commentCandidate));

		inOrder.verify(booksService, times(1)).getById(BOOK_ID);
		inOrder.verify(bookCommentsService, times(1)).save(any());
	}

	@Test
	@DisplayName("should call service for getting book comment by Id")
	public void shouldCallServiceForGettingBookCommentById() {
		assertDoesNotThrow(() -> bookCommentsFacade.getById(COMMENT_ID));
		inOrder.verify(bookCommentsService, times(1)).getById(COMMENT_ID);
	}

	@Test
	@DisplayName("should call service for getting all book comments")
	public void shouldCallServiceForGettingAllBookComments() {
		assertDoesNotThrow(() -> bookCommentsFacade.getAll());
		inOrder.verify(bookCommentsService, times(1)).getAll();
	}

	@Test
	@DisplayName("should call service for delete book comment by id")
	public void shouldCallServiceForDeleteBookCommentById() {
		assertDoesNotThrow(() -> bookCommentsFacade.deleteById(COMMENT_ID));
		inOrder.verify(bookCommentsService, times(1)).deleteById(COMMENT_ID);
	}
}