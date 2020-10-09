package ru.otus.services;

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
import ru.otus.dao.BookCommentsDao;
import ru.otus.domain.Book;
import ru.otus.domain.BookComment;
import ru.otus.domain.Comment;
import ru.otus.exceptions.BookCommentsServiceException;
import ru.otus.validators.FieldValidator;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;

@SpringBootTest
@DisplayName("service for work with book comments")
class BookCommentsServiceImplUnitTest {

	@Configuration
	@Import(BookCommentsServiceImpl.class)
	@EnableConfigurationProperties(AppProperties.class)
	public static class BookCommentsServiceConfig {

	}

	@MockBean
	private BookCommentsDao bookCommentsDao;

	@MockBean
	private FieldValidator fieldValidator;

	@Autowired
	private BookCommentsService bookCommentsService;
	private InOrder inOrder;

	@BeforeEach
	void setUp() {
		inOrder = inOrder(fieldValidator, bookCommentsDao);
	}

	@Test
	@DisplayName("should throw BookCommentsServiceException when book comment for save is null")
	public void shouldThrowExceptionWhenBookCommentForSaveIsNull() {
		assertThrows(BookCommentsServiceException.class, () -> bookCommentsService.save(null));
	}

	@Test
	@DisplayName("should throw BookCommentsServiceException when book comment for save has not valid book field")
	public void shouldThrowExceptionWhenBookCommentForSaveHasNotValidBook() {
		var bookComment = new BookComment(null, new Comment());
		assertThrows(BookCommentsServiceException.class, () -> bookCommentsService.save(bookComment));
	}

	@Test
	@DisplayName("should throw BookCommentsServiceException when book comment for save has not valid comment field")
	public void shouldThrowExceptionWhenBookCommentForSaveHasNotValidComment() {
		var bookComment = new BookComment(new Book(), null);
		assertThrows(BookCommentsServiceException.class, () -> bookCommentsService.save(bookComment));
	}

	@Test
	@DisplayName("should call DAO and field validator for save book comment")
	public void shouldCallDaoAndFieldValidatorForSaveComment() {
		var bookComment = new BookComment(new Book(), new Comment());

		given(fieldValidator.validate(bookComment)).willReturn(true);
		bookCommentsService.save(bookComment);
		inOrder.verify(fieldValidator, times(1)).validate(bookComment);
		inOrder.verify(bookCommentsDao, times(1)).save(bookComment);
	}
}