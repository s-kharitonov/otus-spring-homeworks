package ru.otus.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.domain.Author;
import ru.otus.domain.Book;
import ru.otus.domain.Genre;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@DataMongoTest
@DisplayName("mongo repository for books")
class BooksRepositoryUnitTest {

	private static final String AUTHOR_NAME = "Karen";
	private static final String GENRE_NAME = "Fantasy";

	@Autowired
	private BooksRepository booksRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Test
	@DisplayName("should delete all books by author id")
	@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
	public void shouldDeleteAllByAuthorId() {
		var author = mongoTemplate.findOne(query(where("name").is(AUTHOR_NAME)), Author.class);

		assertThat(author).isNotNull();

		var authorId = author.getId();
		var booksForDelete = mongoTemplate.find(query(where("author.id").is(authorId)), Book.class);

		assertThat(booksForDelete).isNotEmpty();

		booksRepository.deleteAllByAuthor_Id(authorId).block();

		var deletedBooks = mongoTemplate.find(query(where("author.id").is(authorId)), Book.class);

		assertThat(deletedBooks).isEmpty();
	}

	@Test
	@DisplayName("should delete all books by genre id")
	@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
	public void shouldDeleteAllByGenreId() {
		var genre = mongoTemplate.findOne(query(where("name").is(GENRE_NAME)), Genre.class);

		assertThat(genre).isNotNull();

		var genreId = genre.getId();
		var booksForDelete = mongoTemplate.find(query(where("genre.id").is(genreId)), Book.class);

		assertThat(booksForDelete).isNotEmpty();

		booksRepository.deleteAllByGenre_Id(genreId).block();

		var deletedBooks = mongoTemplate.find(query(where("genre.id").is(genreId)), Book.class);

		assertThat(deletedBooks).isEmpty();
	}

	@Test
	@DisplayName("should return all books by author id")
	public void shouldReturnAllBooksByAuthorId() {
		var author = mongoTemplate.findOne(query(where("name").is(AUTHOR_NAME)), Author.class);

		assertThat(author).isNotNull();

		var authorId = author.getId();
		var expectedBooks = mongoTemplate.find(query(where("author.id").is(authorId)), Book.class);
		var books = booksRepository.findAllByAuthor_Id(authorId).toIterable();

		assertThat(expectedBooks).isNotEmpty();
		assertThat(books).isNotEmpty()
				.usingElementComparatorIgnoringFields("author", "genre")
				.containsOnlyOnceElementsOf(expectedBooks);
	}


	@Test
	@DisplayName("should return all books by genre id")
	public void shouldReturnAllBooksByGenreId() {
		var genre = mongoTemplate.findOne(query(where("name").is(GENRE_NAME)), Genre.class);

		assertThat(genre).isNotNull();

		var genreId = genre.getId();
		var expectedBooks = mongoTemplate.find(query(where("genre.id").is(genreId)), Book.class);
		var books = booksRepository.findAllByGenre_Id(genreId).toIterable();

		assertThat(expectedBooks).isNotEmpty();
		assertThat(books).isNotEmpty()
				.usingElementComparatorIgnoringFields("author", "genre")
				.containsOnlyOnceElementsOf(expectedBooks);
	}
}