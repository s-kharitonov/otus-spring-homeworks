package ru.otus.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.otus.configs.AppProperties;
import ru.otus.dao.BookCommentsRepository;
import ru.otus.domain.BookComment;
import ru.otus.exceptions.BookCommentsServiceException;
import ru.otus.validators.FieldValidator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@DisplayName("service for work with comments")
class BookCommentsServiceImplUnitTest {

	private static final String COMMENT = "perfect book!";
	private static final long COMMENT_ID = 1L;
	private static final int NAME_LENGTH_GREATER_THAN_MAX_LENGTH = 300;

	@Configuration
	@Import(BookCommentsServiceImpl.class)
	@EnableConfigurationProperties(AppProperties.class)
	public static class CommentsServiceConfig {

	}

	@MockBean
	private BookCommentsRepository bookCommentsRepository;

	@MockBean
	private FieldValidator fieldValidator;

	@Autowired
	private BookCommentsService bookCommentsService;

	@Test
	@DisplayName("should throw BookCommentsServiceException when comment for save is null")
	public void shouldThrowExceptionWhenCommentForSaveIsNull() {
		assertThrows(BookCommentsServiceException.class, () -> bookCommentsService.save(null));
	}

	@ParameterizedTest
	@NullAndEmptySource
	@MethodSource(value = "createTextWithLengthGreaterThanMaxValue")
	@DisplayName("should throw BookCommentsServiceException when comment for save has not valid text")
	public void shouldThrowExceptionWhenCommentForSaveHasNotValidText(final String text) {
		var comment = new BookComment.Builder().text(text).build();

		given(fieldValidator.validate(comment)).willReturn(false);
		assertThrows(BookCommentsServiceException.class, () -> bookCommentsService.save(comment));
	}

	@Test
	@DisplayName("should return comment by id")
	public void shouldReturnCommentById() {
		var comment = new BookComment.Builder()
				.id(COMMENT_ID)
				.text(COMMENT)
				.build();

		given(bookCommentsRepository.findById(COMMENT_ID)).willReturn(Optional.ofNullable(comment));
		assertThat(bookCommentsService.getById(COMMENT_ID)).get().isEqualToComparingFieldByField(comment);
	}

	@Test
	@DisplayName("should return all comments")
	public void shouldReturnAllComments() {
		var comment = new BookComment.Builder()
				.id(COMMENT_ID)
				.text(COMMENT)
				.build();
		var comments = List.of(comment);

		given(bookCommentsRepository.findAll()).willReturn(comments);
		assertThat(bookCommentsService.getAll()).containsOnlyOnceElementsOf(comments);
	}

	@Test
	@DisplayName("should remove comment by id")
	public void shouldRemoveCommentWithoutThrows() {
		assertDoesNotThrow(() -> bookCommentsService.deleteById(COMMENT_ID));
	}

	private static String[] createTextWithLengthGreaterThanMaxValue() {
		return new String[]{IntStream.range(0, NAME_LENGTH_GREATER_THAN_MAX_LENGTH)
				.mapToObj(String::valueOf)
				.collect(Collectors.joining())};
	}
}