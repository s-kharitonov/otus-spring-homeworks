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
	private static final String EMPTY_NAME = "";
	public static final int NAME_LENGTH_GREATER_THAN_MAX_VALUE = 300;
	private static final String NAME_GREATER_THAN_MAX_VALUE = IntStream.range(0, NAME_LENGTH_GREATER_THAN_MAX_VALUE)
			.mapToObj(String::valueOf)
			.collect(Collectors.joining());

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
		var genre = new Genre(GENRE_NAME);

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

	@Test
	@DisplayName("should throw GenresServiceException when genre for create has null name")
	public void shouldThrowExceptionWhenGenreForCreateHasNullName() {
		var genre = new Genre(null);

		given(fieldValidator.validate(genre)).willReturn(false);
		given(localizationService.localizeMessage(Constants.INVALID_GENRE_MSG_KEY, genre)).willReturn(EMPTY_APP_MESSAGE);
		assertThrows(GenresServiceException.class, () -> genresService.saveGenre(genre));
	}

	@Test
	@DisplayName("should throw GenresServiceException when genre for create has empty name")
	public void shouldThrowExceptionWhenGenreForCreateHasEmptyName() {
		var genre = new Genre(EMPTY_NAME);

		given(fieldValidator.validate(genre)).willReturn(false);
		given(localizationService.localizeMessage(Constants.INVALID_GENRE_MSG_KEY, genre)).willReturn(EMPTY_APP_MESSAGE);
		assertThrows(GenresServiceException.class, () -> genresService.saveGenre(genre));
	}

	@Test
	@DisplayName("should throw GenresServiceException when genre for create has name length greater than max value")
	public void shouldThrowExceptionWhenGenreForCreateHasNameLengthGreaterThanMaxValue() {
		var genre = new Genre(NAME_GREATER_THAN_MAX_VALUE);

		given(fieldValidator.validate(genre)).willReturn(false);
		given(localizationService.localizeMessage(Constants.INVALID_GENRE_MSG_KEY, genre)).willReturn(EMPTY_APP_MESSAGE);
		assertThrows(GenresServiceException.class, () -> genresService.saveGenre(genre));
	}

	@Test
	@DisplayName("should return genre by id")
	public void shouldReturnGenreById() {
		var genre = new Genre(FIRST_GENRE_ID, GENRE_NAME);

		given(genresDao.findGenreById(FIRST_GENRE_ID)).willReturn(Optional.of(genre));
		assertEquals(genre.getId(), genresService.getGenreById(FIRST_GENRE_ID).orElseThrow().getId());
	}

	@Test
	@DisplayName("should return all genres")
	public void shouldReturnAllGenres() {
		var genres = List.of(new Genre(FIRST_GENRE_ID, GENRE_NAME));

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

	@Test
	@DisplayName("should throw GenresServiceException when genre for update has null name")
	public void shouldThrowExceptionWhenGenreForUpdateHasNullName() {
		var genre = new Genre(FIRST_GENRE_ID,null);

		given(fieldValidator.validate(genre)).willReturn(false);
		given(localizationService.localizeMessage(Constants.INVALID_GENRE_MSG_KEY, genre)).willReturn(EMPTY_APP_MESSAGE);
		assertThrows(GenresServiceException.class, () -> genresService.updateGenre(genre));
	}

	@Test
	@DisplayName("should throw GenresServiceException when genre for update has empty name")
	public void shouldThrowExceptionWhenGenreForUpdateHasEmptyName() {
		var genre = new Genre(FIRST_GENRE_ID, EMPTY_NAME);

		given(fieldValidator.validate(genre)).willReturn(false);
		given(localizationService.localizeMessage(Constants.INVALID_GENRE_MSG_KEY, genre)).willReturn(EMPTY_APP_MESSAGE);
		assertThrows(GenresServiceException.class, () -> genresService.updateGenre(genre));
	}

	@Test
	@DisplayName("should throw GenresServiceException when genre for update has name length greater than max value")
	public void shouldThrowExceptionWhenGenreForUpdateHasNameLengthGreaterThanMaxValue() {
		var genre = new Genre(FIRST_GENRE_ID, NAME_GREATER_THAN_MAX_VALUE);

		given(fieldValidator.validate(genre)).willReturn(false);
		given(localizationService.localizeMessage(Constants.INVALID_GENRE_MSG_KEY, genre)).willReturn(EMPTY_APP_MESSAGE);
		assertThrows(GenresServiceException.class, () -> genresService.saveGenre(genre));
	}
}