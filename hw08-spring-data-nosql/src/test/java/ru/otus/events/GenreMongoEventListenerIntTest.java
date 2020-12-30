package ru.otus.events;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.domain.Book;
import ru.otus.services.BooksService;
import ru.otus.services.GenresService;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("Mongo DB genre event listener")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class GenreMongoEventListenerIntTest {

	private static final String GENRE_NAME = "Horror";

	@Autowired
	private GenresService genresService;

	@Autowired
	private BooksService booksService;

	@Test
	@DisplayName("should delete related books after delete genre")
	public void shouldDeleteRelatedBooksAfterDeleteGenre() {
		var genre = genresService.getAll().get(0);

		assertThat(genre).isNotNull();

		var genreId = genre.getId();
		var booksForDelete = booksService.getByGenreId(genreId);

		assertThat(booksForDelete).isNotEmpty();

		genresService.deleteById(genreId);

		var deletedBooks = booksService.getByAuthorId(genreId);

		assertThat(deletedBooks).isEmpty();
	}

	@Test
	@DisplayName("should update genre in related books after save genre")
	public void shouldUpdateAuthorInRelatedBooksAfterSave() {
		var genre = genresService.getAll().get(0);

		assertThat(genre).isNotNull();

		var genreId = genre.getId();
		var booksForUpdate = booksService.getByGenreId(genreId);

		assertThat(booksForUpdate).isNotEmpty();
		genre.setName(GENRE_NAME);

		genresService.save(genre);

		var updatedGenresInBooks = booksService.getByGenreId(genreId).stream()
				.map(Book::getGenre).collect(toList());

		assertThat(updatedGenresInBooks).isNotEmpty()
				.usingFieldByFieldElementComparator()
				.containsOnly(genre);
	}
}