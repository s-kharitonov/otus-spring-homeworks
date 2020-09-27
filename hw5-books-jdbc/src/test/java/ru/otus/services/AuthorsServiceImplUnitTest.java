package ru.otus.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.otus.configs.AppProperties;
import ru.otus.dao.AuthorsDao;
import ru.otus.domain.Author;
import ru.otus.domain.Constants;
import ru.otus.exceptions.AuthorsServiceException;
import ru.otus.validators.FieldValidator;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@DisplayName("service for work with authors")
@SpringBootTest
class AuthorsServiceImplUnitTest {

	private static final String SURNAME = "kharitonov";
	private static final String NAME = "sergey";
	private static final long FIRST_AUTHOR_ID = 1L;
	private static final String EMPTY_NAME = "";
	private static final String EMPTY_SURNAME = "";
	private static final String EMPTY_APP_MESSAGE = "";

	@Import(AuthorsServiceImpl.class)
	@Configuration
	@EnableConfigurationProperties(AppProperties.class)
	public static class AuthorsServiceConfig {
	}

	@MockBean
	private AuthorsDao authorsDao;

	@MockBean
	private LocalizationService localizationService;

	@MockBean
	private FieldValidator fieldValidator;

	@Autowired
	private AuthorsService authorsService;

	@Test
	@DisplayName("should create author")
	public void shouldCreateAuthor() {
		var author = new Author(NAME, SURNAME);
		given(fieldValidator.validate(author)).willReturn(true);
		given(authorsDao.saveAuthor(author)).willReturn(Optional.of(FIRST_AUTHOR_ID));
		var id = authorsService.createAuthor(author).orElseThrow();
		assertEquals(FIRST_AUTHOR_ID, id);
	}

	@Test
	@DisplayName("should throw AuthorsServiceException when author for create is null")
	public void shouldThrowExceptionWhenAuthorForCreateIsNull() {
		Author author = null;
		given(localizationService.localizeMessage(Constants.INVALID_AUTHOR_MSG_KEY, author)).willReturn(EMPTY_APP_MESSAGE);
		assertThrows(AuthorsServiceException.class, () -> authorsService.createAuthor(author));
	}

	@Test
	@DisplayName("should return author by id")
	public void shouldReturnAuthorById() {
		var author = new Author(FIRST_AUTHOR_ID, NAME, SURNAME);
		given(authorsDao.findAuthorById(FIRST_AUTHOR_ID)).willReturn(Optional.of(author));
		var foundedAuthor = authorsService.getAuthorById(FIRST_AUTHOR_ID).orElseThrow();
		assertEquals(author.getId(), foundedAuthor.getId());
	}

	@Test
	@DisplayName("should return all authors")
	public void shouldReturnAllAuthors() {
		var authors = List.of(new Author(FIRST_AUTHOR_ID, NAME, SURNAME));
		given(authorsDao.findAllAuthors()).willReturn(authors);
		assertFalse(authorsService.getAllAuthors().isEmpty());
	}

	@Test
	@DisplayName("should throw AuthorsServiceException when author for update is null")
	public void shouldThrowExceptionWhenAuthorForUpdateIsNull() {
		Author author = null;
		given(localizationService.localizeMessage(Constants.INVALID_AUTHOR_MSG_KEY, author)).willReturn(EMPTY_APP_MESSAGE);
		assertThrows(AuthorsServiceException.class, () -> authorsService.updateAuthor(author));
	}

	@Test
	@DisplayName("should throw AuthorsServiceException when author for update has null fields")
	public void shouldThrowExceptionWhenAuthorForUpdateHasNullFields() {
		var author = new Author(null, null);
		given(fieldValidator.validate(author)).willReturn(false);
		given(localizationService.localizeMessage(Constants.INVALID_AUTHOR_MSG_KEY, author)).willReturn(EMPTY_APP_MESSAGE);
		assertThrows(AuthorsServiceException.class, () -> authorsService.updateAuthor(author));
	}

	@Test
	@DisplayName("should throw AuthorsServiceException when author for update has empty fields")
	public void shouldThrowExceptionWhenAuthorForUpdateHasEmptyFields() {
		var author = new Author(EMPTY_NAME, EMPTY_SURNAME);
		given(fieldValidator.validate(author)).willReturn(false);
		given(localizationService.localizeMessage(Constants.INVALID_AUTHOR_MSG_KEY, author)).willReturn(EMPTY_APP_MESSAGE);
		assertThrows(AuthorsServiceException.class, () -> authorsService.updateAuthor(author));
	}


	@Test
	@DisplayName("should throw AuthorsServiceException when author for create has null fields")
	public void shouldThrowExceptionWhenAuthorForCreateHasNullFields() {
		var author = new Author(null, null);
		given(fieldValidator.validate(author)).willReturn(false);
		given(localizationService.localizeMessage(Constants.INVALID_AUTHOR_MSG_KEY, author)).willReturn(EMPTY_APP_MESSAGE);
		assertThrows(AuthorsServiceException.class, () -> authorsService.createAuthor(author));
	}

	@Test
	@DisplayName("should throw AuthorsServiceException when author for create has empty fields")
	public void shouldThrowExceptionWhenAuthorForCreateHasEmptyFields() {
		var author = new Author(EMPTY_NAME, EMPTY_SURNAME);
		given(fieldValidator.validate(author)).willReturn(false);
		given(localizationService.localizeMessage(Constants.INVALID_AUTHOR_MSG_KEY, author)).willReturn(EMPTY_APP_MESSAGE);
		assertThrows(AuthorsServiceException.class, () -> authorsService.createAuthor(author));
	}
}