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
import ru.otus.dao.GenresDao;
import ru.otus.domain.Constants;
import ru.otus.domain.Genre;
import ru.otus.exceptions.GenresServiceException;
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
	private static final String EMPTY_APP_MESSAGE = "";
	private static final int NAME_LENGTH_GREATER_THAN_MAX_LENGTH = 300;

	@Configuration
	@EnableConfigurationProperties(AppProperties.class)
	@Import(GenresServiceImpl.class)
	public static class GenresServiceConfig {

	}

	@MockBean
	private FieldValidator fieldValidator;

	@MockBean
	private LocalizationService localizationService;

	@MockBean
	private GenresDao genresDao;

	@Autowired
	private GenresService genresService;

	@Test
	@DisplayName("should save genre")
	public void shouldSaveGenre() {
		var genre = new Genre.Builder().name(GENRE_NAME).build();

		given(fieldValidator.validate(genre)).willReturn(true);
		given(genresDao.saveGenre(genre)).willReturn(Optional.of(FIRST_GENRE_ID));

		var genreId = genresService.saveGenre(genre).orElseThrow();

		assertEquals(FIRST_GENRE_ID, genreId);
	}

	@Test
	@DisplayName("should throw GenresServiceException when genre for create is null")
	public void shouldThrowExceptionWhenGenreForCreateIsNull() {
		Genre genre = null;

		given(localizationService.localizeMessage(Constants.INVALID_GENRE_MSG_KEY, genre)).willReturn(EMPTY_APP_MESSAGE);
		assertThrows(GenresServiceException.class, () -> genresService.saveGenre(genre));
	}

	@ParameterizedTest
	@DisplayName("should throw GenresServiceException when genre for create has not valid name")
	@NullAndEmptySource
	@MethodSource(value = "createNameGreaterThanMaxLength")
	public void shouldThrowExceptionWhenGenreForCreateHasNotValidName(final String name) {
		var genre = new Genre.Builder().name(name).build();

		given(fieldValidator.validate(genre)).willReturn(false);
		given(localizationService.localizeMessage(Constants.INVALID_GENRE_MSG_KEY, genre)).willReturn(EMPTY_APP_MESSAGE);
		assertThrows(GenresServiceException.class, () -> genresService.saveGenre(genre));
	}

	@Test
	@DisplayName("should return genre by id")
	public void shouldReturnGenreById() {
		var genre = new Genre.Builder()
				.id(FIRST_GENRE_ID)
				.name(GENRE_NAME)
				.build();

		given(genresDao.findGenreById(FIRST_GENRE_ID)).willReturn(Optional.of(genre));
		assertEquals(genre.getId(), genresService.getGenreById(FIRST_GENRE_ID).orElseThrow().getId());
	}

	@Test
	@DisplayName("should return all genres")
	public void shouldReturnAllGenres() {
		var genre = new Genre.Builder()
				.id(FIRST_GENRE_ID)
				.name(GENRE_NAME)
				.build();
		var genres = List.of(genre);

		given(genresDao.findAllGenres()).willReturn(genres);
		assertFalse(genresService.getAllGenres().isEmpty());
	}

	@Test
	@DisplayName("should throw GenresServiceException when genre for update is null")
	public void shouldThrowExceptionWhenGenreForUpdateIsNull() {
		Genre genre = null;

		given(localizationService.localizeMessage(Constants.INVALID_GENRE_MSG_KEY, genre)).willReturn(EMPTY_APP_MESSAGE);
		assertThrows(GenresServiceException.class, () -> genresService.updateGenre(genre));
	}

	@ParameterizedTest
	@DisplayName("should throw GenresServiceException when genre for update has not valid name")
	@NullAndEmptySource
	@MethodSource(value = "createNameGreaterThanMaxLength")
	public void shouldThrowExceptionWhenGenreForUpdateHasNotValidName(final String name) {
		var genre = new Genre.Builder().name(name).build();

		given(fieldValidator.validate(genre)).willReturn(false);
		given(localizationService.localizeMessage(Constants.INVALID_GENRE_MSG_KEY, genre)).willReturn(EMPTY_APP_MESSAGE);
		assertThrows(GenresServiceException.class, () -> genresService.saveGenre(genre));
	}

	private static String[] createNameGreaterThanMaxLength() {
		return new String[]{IntStream.range(0, NAME_LENGTH_GREATER_THAN_MAX_LENGTH)
				.mapToObj(String::valueOf)
				.collect(Collectors.joining())};
	}
}