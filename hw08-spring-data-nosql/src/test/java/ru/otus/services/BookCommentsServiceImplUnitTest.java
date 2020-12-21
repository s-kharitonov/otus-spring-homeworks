package ru.otus.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.otus.domain.Author;
import ru.otus.domain.Book;
import ru.otus.domain.BookComment;
import ru.otus.domain.Genre;
import ru.otus.exсeptions.BookCommentsServiceException;
import ru.otus.repositories.BookCommentsRepository;
import ru.otus.validators.FieldValidator;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@DisplayName("service for book comments")
@SpringBootTest
class BookCommentsServiceImplUnitTest {

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
	@Import(BookCommentsServiceImpl.class)
	@EnableConfigurationProperties
	public static class CommentsServiceConfig {

	}

	@MockBean
	private BookCommentsRepository bookCommentsRepository;

	@MockBean
	private FieldValidator fieldValidator;

	@Autowired
	private BookCommentsService bookCommentsService;

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
	}

	@Test
	@DisplayName("should save comment")
	public void shouldSaveComment() {
		var comment = new BookComment.Builder()
				.text(COMMENT)
				.book(book)
				.build();

		given(fieldValidator.validate(comment)).willReturn(true);
		given(bookCommentsRepository.save(comment)).willReturn(comment);
		assertDoesNotThrow(() -> bookCommentsService.save(comment));
	}

	@Test
	@DisplayName("should throw BookCommentsServiceException when comment for save is null")
	public void shouldThrowExceptionWhenCommentForSaveIsNull() {
		assertThrows(BookCommentsServiceException.class, () -> bookCommentsService.save(null));
	}

	@ParameterizedTest
	@NullAndEmptySource
	@DisplayName("should throw BookCommentsServiceException when comment for save has not valid text")
	public void shouldThrowExceptionWhenCommentForSaveHasNotValidText(final String text) {
		var comment = new BookComment.Builder()
				.text(text)
				.book(book)
				.build();

		given(fieldValidator.validate(comment)).willReturn(false);
		assertThrows(BookCommentsServiceException.class, () -> bookCommentsService.save(comment));
	}

	@Test
	@DisplayName("should save all comments")
	public void shouldSaveAllComments() {
		var comment = new BookComment.Builder()
				.text(COMMENT)
				.book(book)
				.build();
		var comments = List.of(comment);

		given(fieldValidator.validate(comment)).willReturn(true);
		given(bookCommentsRepository.saveAll(comments)).willReturn(comments);
		assertDoesNotThrow(() -> bookCommentsService.saveAll(comments));
	}

	@Test
	@DisplayName("should throw exception when comments for save is null")
	public void shouldThrowExceptionWhenCommentsForSaveIsNull() {
		assertThrows(BookCommentsServiceException.class, () -> bookCommentsService.saveAll(null));
	}

	@Test
	@DisplayName("should throw exception when comments for save has invalid fields")
	public void shouldThrowExceptionWhenCommentsForSaveHasInvalidFields() {
		var comment = new BookComment.Builder()
				.text(null)
				.book(null)
				.build();
		assertThrows(BookCommentsServiceException.class, () -> bookCommentsService.saveAll(List.of(comment)));
	}

	@Test
	@DisplayName("should return comment by id")
	public void shouldReturnCommentById() {
		var comment = new BookComment.Builder()
				.id(COMMENT_ID)
				.text(COMMENT)
				.book(book)
				.build();

		given(bookCommentsRepository.findById(COMMENT_ID)).willReturn(Optional.ofNullable(comment));
		assertThat(bookCommentsService.getById(COMMENT_ID)).get().isEqualToComparingFieldByField(comment);
	}

	@Test
	@DisplayName("should return by book id")
	public void shouldReturnByBookId() {
		var comment = new BookComment.Builder()
				.id(COMMENT_ID)
				.text(COMMENT)
				.book(book)
				.build();
		var comments = List.of(comment);

		given(bookCommentsRepository.findAllByBook_Id(BOOK_ID)).willReturn(comments);
		assertThat(bookCommentsService.getByBookId(BOOK_ID)).isNotEmpty().containsOnlyOnceElementsOf(comments);
	}

	@ParameterizedTest
	@NullAndEmptySource
	@DisplayName("should throw exception when book id for get by book id is not valid")
	public void shouldThrowExceptionWhenBookIdForGetByBookIdIsNotValid(final String bookId) {
		assertThrows(BookCommentsServiceException.class, () -> bookCommentsService.getByBookId(bookId));
	}

	@Test
	@DisplayName("should return by books ids")
	public void shouldReturnByBooksIds() {
		var comment = new BookComment.Builder()
				.id(COMMENT_ID)
				.text(COMMENT)
				.book(book)
				.build();
		var comments = List.of(comment);

		given(bookCommentsRepository.findAllByBook_IdIn(List.of(BOOK_ID))).willReturn(comments);
		assertThat(bookCommentsService.getByBooksIds(List.of(BOOK_ID))).isNotEmpty().containsOnlyOnceElementsOf(comments);
	}

	@Test
	@DisplayName("should throw exception when books ids for get by books ids is not valid")
	public void shouldThrowExceptionWhenBooksIdsForGetByBooksIdsIsNotValid() {
		assertThrows(BookCommentsServiceException.class, () -> bookCommentsService.getByBooksIds(null));
	}

	@Test
	@DisplayName("should return all comments")
	public void shouldReturnAllComments() {
		var comment = new BookComment.Builder()
				.id(COMMENT_ID)
				.text(COMMENT)
				.book(book)
				.build();
		var comments = List.of(comment);

		given(bookCommentsRepository.findAll()).willReturn(comments);
		assertThat(bookCommentsService.getAll()).containsOnlyOnceElementsOf(comments);
	}

	@Test
	@DisplayName("should delete comment by id")
	public void shouldDeleteCommentWithoutThrows() {
		assertDoesNotThrow(() -> bookCommentsService.deleteById(COMMENT_ID));
	}

	@ParameterizedTest
	@NullAndEmptySource
	@DisplayName("should throw exception when comment id for delete by comment id is invalid")
	public void shouldThrowExceptionWhenCommentIdForDeleteByCommentIdIsInvalid(final String commentId) {
		assertThrows(BookCommentsServiceException.class, () -> bookCommentsService.deleteById(commentId));
	}

	@Test
	@DisplayName("should delete comment by book id")
	public void shouldDeleteCommentByBookIdWithoutThrows() {
		assertDoesNotThrow(() -> bookCommentsService.deleteByBookId(BOOK_ID));
	}

	@ParameterizedTest
	@NullAndEmptySource
	@DisplayName("should throw exception when book id for delete by book id is invalid")
	public void shouldThrowExceptionWhenBookIdForDeleteByBookIdIsInvalid(final String bookId) {
		assertThrows(BookCommentsServiceException.class, () -> bookCommentsService.deleteByBookId(bookId));
	}

	@Test
	@DisplayName("should delete comment by genre id")
	public void shouldDeleteCommentByGenreIdWithoutThrows() {
		assertDoesNotThrow(() -> bookCommentsService.deleteByGenreId(GENRE_ID));
	}

	@ParameterizedTest
	@NullAndEmptySource
	@DisplayName("should throw exception when genre id for delete by genre id is invalid")
	public void shouldThrowExceptionWhenGenreIdForDeleteByGenreIdIsInvalid(final String genreId) {
		assertThrows(BookCommentsServiceException.class, () -> bookCommentsService.deleteByGenreId(genreId));
	}

	@Test
	@DisplayName("should delete comment by author id")
	public void shouldDeleteCommentByAuthorIdWithoutThrows() {
		assertDoesNotThrow(() -> bookCommentsService.deleteByAuthorId(AUTHOR_ID));
	}

	@ParameterizedTest
	@NullAndEmptySource
	@DisplayName("should throw exception when author id for delete by author id is invalid")
	public void shouldThrowExceptionWhenAuthorIdForDeleteByAuthorIdIsInvalid(final String authorId) {
		assertThrows(BookCommentsServiceException.class, () -> bookCommentsService.deleteByAuthorId(authorId));
	}
}