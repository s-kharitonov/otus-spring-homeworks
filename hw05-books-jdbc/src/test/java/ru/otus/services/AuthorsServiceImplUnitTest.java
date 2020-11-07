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
import ru.otus.dao.AuthorsDao;
import ru.otus.domain.Author;
import ru.otus.exceptions.AuthorsServiceException;
import ru.otus.validators.FieldValidator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@DisplayName("service for work with authors")
@SpringBootTest
class AuthorsServiceImplUnitTest {

	private static final String SURNAME = "kharitonov";
	private static final String NAME = "sergey";
	private static final long FIRST_AUTHOR_ID = 1L;
	private static final int NAME_LENGTH_GREATER_THAN_MAX_LENGTH = 300;

	@Import(AuthorsServiceImpl.class)
	@Configuration
	@EnableConfigurationProperties(AppProperties.class)
	public static class AuthorsServiceConfig {
	}

	@MockBean
	private AuthorsDao authorsDao;

	@MockBean
	private FieldValidator fieldValidator;

	@Autowired
	private AuthorsService authorsService;

	@Test
	@DisplayName("should create author")
	public void shouldCreateAuthor() {
		var author = new Author.Builder()
				.name(NAME)
				.surname(SURNAME)
				.build();
		given(fieldValidator.validate(author)).willReturn(true);
		given(authorsDao.saveAuthor(author)).willReturn(Optional.of(FIRST_AUTHOR_ID));
		var id = authorsService.createAuthor(author).orElseThrow();
		assertEquals(FIRST_AUTHOR_ID, id);
	}

	@Test
	@DisplayName("should throw AuthorsServiceException when author for create is null")
	public void shouldThrowExceptionWhenAuthorForCreateIsNull() {
		Author author = null;
		assertThrows(AuthorsServiceException.class, () -> authorsService.createAuthor(author));
	}

	@ParameterizedTest
	@NullAndEmptySource
	@MethodSource("createStringGreaterThanMaxLength")
	@DisplayName("should throw AuthorsServiceException when author for create has not valid name")
	public void shouldThrowExceptionWhenAuthorForCreateHasNotValidName(final String name) {
		var author = new Author.Builder()
				.name(name)
				.surname(SURNAME)
				.build();
		given(fieldValidator.validate(author)).willReturn(false);
		assertThrows(AuthorsServiceException.class, () -> authorsService.createAuthor(author));
	}

	@ParameterizedTest
	@NullAndEmptySource
	@MethodSource("createStringGreaterThanMaxLength")
	@DisplayName("should throw AuthorsServiceException when author for create has not valid surname")
	public void shouldThrowExceptionWhenAuthorForCreateHasNotValidSurname(final String surname) {
		var author = new Author.Builder()
				.name(NAME)
				.surname(surname)
				.build();
		given(fieldValidator.validate(author)).willReturn(false);
		assertThrows(AuthorsServiceException.class, () -> authorsService.createAuthor(author));
	}

	@Test
	@DisplayName("should return author by id")
	public void shouldReturnAuthorById() {
		var author = new Author.Builder()
				.id(FIRST_AUTHOR_ID)
				.name(NAME)
				.surname(SURNAME)
				.build();
		given(authorsDao.findAuthorById(FIRST_AUTHOR_ID)).willReturn(Optional.of(author));
		var foundedAuthor = authorsService.getAuthorById(FIRST_AUTHOR_ID).orElseThrow();
		assertEquals(author.getId(), foundedAuthor.getId());
	}

	@Test
	@DisplayName("should return all authors")
	public void shouldReturnAllAuthors() {
		var author = new Author.Builder()
				.id(FIRST_AUTHOR_ID)
				.name(NAME)
				.surname(SURNAME)
				.build();
		var authors = List.of(author);
		given(authorsDao.findAllAuthors()).willReturn(authors);
		assertFalse(authorsService.getAllAuthors().isEmpty());
	}

	@Test
	@DisplayName("should remove author")
	public void shouldRemoveAuthor() {
		given(authorsDao.removeAuthor(FIRST_AUTHOR_ID)).willReturn(true);
		assertTrue(authorsService.removeAuthor(FIRST_AUTHOR_ID));
	}

	@Test
	@DisplayName("should throw AuthorsServiceException when author for update is null")
	public void shouldThrowExceptionWhenAuthorForUpdateIsNull() {
		Author author = null;
		assertThrows(AuthorsServiceException.class, () -> authorsService.updateAuthor(author));
	}

	@ParameterizedTest
	@NullAndEmptySource
	@MethodSource("createStringGreaterThanMaxLength")
	@DisplayName("should throw AuthorsServiceException when author for update has not valid name")
	public void shouldThrowExceptionWhenAuthorForUpdateHasNotValidName(final String name) {
		var author = new Author.Builder()
				.name(name)
				.surname(SURNAME)
				.build();
		given(fieldValidator.validate(author)).willReturn(false);
		assertThrows(AuthorsServiceException.class, () -> authorsService.updateAuthor(author));
	}

	@ParameterizedTest
	@NullAndEmptySource
	@MethodSource("createStringGreaterThanMaxLength")
	@DisplayName("should throw AuthorsServiceException when author for update has not valid surname")
	public void shouldThrowExceptionWhenAuthorForUpdateHasNotValidSurname(final String surname) {
		var author = new Author.Builder()
				.name(NAME)
				.surname(surname)
				.build();
		given(fieldValidator.validate(author)).willReturn(false);
		assertThrows(AuthorsServiceException.class, () -> authorsService.updateAuthor(author));
	}

	private static String[] createStringGreaterThanMaxLength() {
		return new String[]{IntStream.range(0, NAME_LENGTH_GREATER_THAN_MAX_LENGTH)
				.mapToObj(String::valueOf)
				.collect(Collectors.joining())};
	}
}