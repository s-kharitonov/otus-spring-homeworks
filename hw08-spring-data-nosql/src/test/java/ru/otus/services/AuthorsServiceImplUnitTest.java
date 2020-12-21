package ru.otus.services;

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
import ru.otus.exсeptions.AuthorsServiceException;
import ru.otus.repositories.AuthorsRepository;
import ru.otus.validators.FieldValidator;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@DisplayName("service for authors")
@SpringBootTest
class AuthorsServiceImplUnitTest {

	private static final String SURNAME = "kharitonov";
	private static final String NAME = "sergey";
	private static final String FIRST_AUTHOR_ID = "1";

	@Import(AuthorsServiceImpl.class)
	@Configuration
	@EnableConfigurationProperties
	public static class AuthorServiceConfig {

	}

	@Autowired
	private AuthorsService authorsService;

	@MockBean
	private AuthorsRepository authorsRepository;

	@MockBean
	private FieldValidator fieldValidator;

	@Test
	@DisplayName("should throw AuthorsServiceException when author for save is null")
	public void shouldThrowExceptionWhenAuthorForSaveIsNull() {
		Author author = null;
		assertThrows(AuthorsServiceException.class, () -> authorsService.save(author));
	}

	@ParameterizedTest
	@NullAndEmptySource
	@DisplayName("should throw AuthorsServiceException when author for save has not valid name")
	public void shouldThrowExceptionWhenAuthorForSaveHasNotValidName(final String name) {
		var author = new Author.Builder()
				.name(name)
				.surname(SURNAME)
				.build();
		given(fieldValidator.validate(author)).willReturn(false);
		assertThrows(AuthorsServiceException.class, () -> authorsService.save(author));
	}

	@ParameterizedTest
	@NullAndEmptySource
	@DisplayName("should throw AuthorsServiceException when author for save has not valid surname")
	public void shouldThrowExceptionWhenAuthorForSaveHasNotValidSurname(final String surname) {
		var author = new Author.Builder()
				.name(NAME)
				.surname(surname)
				.build();
		given(fieldValidator.validate(author)).willReturn(false);
		assertThrows(AuthorsServiceException.class, () -> authorsService.save(author));
	}

	@Test
	@DisplayName("should return author by id")
	public void shouldReturnAuthorById() {
		var author = new Author.Builder()
				.id(FIRST_AUTHOR_ID)
				.name(NAME)
				.surname(SURNAME)
				.build();
		given(authorsRepository.findById(FIRST_AUTHOR_ID)).willReturn(Optional.of(author));
		var foundedAuthor = authorsService.getById(FIRST_AUTHOR_ID).orElseThrow();
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
		given(authorsRepository.findAll()).willReturn(authors);
		assertFalse(authorsService.getAll().isEmpty());
	}

	@Test
	@DisplayName("should delete author without errors")
	public void shouldDeleteAuthorWithoutThrows() {
		assertDoesNotThrow(() -> authorsService.deleteById(FIRST_AUTHOR_ID));
	}
}