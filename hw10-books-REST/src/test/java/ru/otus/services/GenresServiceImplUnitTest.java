package ru.otus.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.otus.domain.Genre;
import ru.otus.exceptions.GenresServiceException;
import ru.otus.repositories.GenresRepository;
import ru.otus.validators.FieldValidator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@DisplayName("service for work with genres")
@SpringBootTest
class GenresServiceImplUnitTest {

	private static final String GENRE_NAME = "Horror";
	private static final long FIRST_GENRE_ID = 1L;
	private static final int NAME_LENGTH_GREATER_THAN_MAX_LENGTH = 300;

	@Configuration
	@Import(GenresServiceImpl.class)
	public static class GenresServiceConfig {

	}

	@MockBean
	private FieldValidator fieldValidator;

	@MockBean
	private GenresRepository genresRepository;

	@Autowired
	private GenresService genresService;

	@Test
	@DisplayName("should throw GenresServiceException when genre for create is null")
	public void shouldThrowExceptionWhenGenreForCreateIsNull() {
		Genre genre = null;
		assertThrows(GenresServiceException.class, () -> genresService.save(genre));
	}

	@ParameterizedTest
	@DisplayName("should throw GenresServiceException when genre for create has not valid name")
	@NullAndEmptySource
	@MethodSource(value = "createNameGreaterThanMaxLength")
	public void shouldThrowExceptionWhenGenreForCreateHasNotValidName(final String name) {
		var genre = new Genre.Builder().name(name).build();

		given(fieldValidator.validate(genre)).willReturn(false);
		assertThrows(GenresServiceException.class, () -> genresService.save(genre));
	}

	@Test
	@DisplayName("should return genre by id")
	public void shouldReturnGenreById() {
		var genre = new Genre.Builder()
				.id(FIRST_GENRE_ID)
				.name(GENRE_NAME)
				.build();

		given(genresRepository.findById(FIRST_GENRE_ID)).willReturn(Optional.of(genre));
		assertEquals(genre.getId(), genresService.getById(FIRST_GENRE_ID).orElseThrow().getId());
	}

	@Test
	@DisplayName("should return all genres")
	public void shouldReturnAllGenres() {
		var genre = new Genre.Builder()
				.id(FIRST_GENRE_ID)
				.name(GENRE_NAME)
				.build();
		var genres = List.of(genre);

		given(genresRepository.findAll()).willReturn(genres);
		assertFalse(genresService.getAll().isEmpty());
	}

	@Test
	@DisplayName("should remove genre")
	public void shouldRemoveGenreWithoutThrows() {
		assertDoesNotThrow(() -> genresService.deleteById(FIRST_GENRE_ID));
	}

	@ParameterizedTest
	@DisplayName("should throw GenresServiceException when genre for update has not valid name")
	@NullAndEmptySource
	@MethodSource(value = "createNameGreaterThanMaxLength")
	public void shouldThrowExceptionWhenGenreForUpdateHasNotValidName(final String name) {
		var genre = new Genre.Builder().name(name).build();

		given(fieldValidator.validate(genre)).willReturn(false);
		assertThrows(GenresServiceException.class, () -> genresService.save(genre));
	}

	private static String[] createNameGreaterThanMaxLength() {
		return new String[]{IntStream.range(0, NAME_LENGTH_GREATER_THAN_MAX_LENGTH)
				.mapToObj(String::valueOf)
				.collect(Collectors.joining())};
	}
}