package ru.otus.services.facades;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.otus.domain.*;
import ru.otus.services.BookCommentsService;
import ru.otus.services.BooksService;

import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;

@SpringBootTest
@DisplayName("facade for work with book comments")
class BookCommentsFacadeImplUnitTest {

	private static final String BOOK_NAME = "name";
	private static final long AUTHOR_ID = 1L;
	private static final long GENRE_ID = 1L;
	private static final long BOOK_ID = 1L;
	private static final Date BOOK_PUBLICATION_DATE = new Date();
	private static final int BOOK_PRINT_LENGTH = 300;
	private static final String AUTHOR_NAME = "name";
	private static final String AUTHOR_SURNAME = "surname";
	private static final String GENRE_NAME = "name";
	private static final long BOOK_COMMENT_ID = 1L;
	private static final String BOOK_COMMENT = "comment";

	@Configuration
	@Import(BookCommentsFacadeImpl.class)
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
		this.inOrder = inOrder(booksService, bookCommentsService);
		this.book = new Book.Builder()
				.id(BOOK_ID)
				.name(BOOK_NAME)
				.publicationDate(BOOK_PUBLICATION_DATE)
				.printLength(BOOK_PRINT_LENGTH)
				.author(buildAuthor())
				.genre(buildGenre())
				.build();
	}

	@Test
	@DisplayName("should call services for save book comment")
	public void shouldCallServicesForSaveBookComment() {
		var candidate = buildBookCommentCandidate();
		var expectedBookComment = new BookComment.Builder()
				.book(book)
				.build();

		given(booksService.getById(BOOK_ID)).willReturn(Optional.of(book));
		given(bookCommentsService.save(any())).willReturn(expectedBookComment);

		bookCommentsFacade.create(candidate);

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
		bookCommentsFacade.deleteById(BOOK_COMMENT_ID);
		inOrder.verify(bookCommentsService, times(1)).deleteById(BOOK_COMMENT_ID);
	}

	@Test
	@DisplayName("should call service for update book comment")
	public void shouldCallServiceForUpdateBookComment() {
		var bookComment = new BookComment.Builder()
				.book(book)
				.build();

		bookCommentsFacade.update(bookComment);
		inOrder.verify(bookCommentsService, times(1)).save(bookComment);
	}

	private BookCommentCandidate buildBookCommentCandidate() {
		var candidate = new BookCommentCandidate();

		candidate.setBookId(BOOK_ID);
		candidate.setText(BOOK_COMMENT);

		return candidate;
	}

	private Genre buildGenre() {
		return new Genre.Builder()
				.id(GENRE_ID)
				.name(GENRE_NAME)
				.build();
	}

	private Author buildAuthor() {
		return new Author.Builder()
				.id(AUTHOR_ID)
				.name(AUTHOR_NAME)
				.surname(AUTHOR_SURNAME)
				.build();
	}
}