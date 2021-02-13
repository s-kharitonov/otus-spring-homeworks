package ru.otus.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.domain.Author;
import ru.otus.domain.Book;
import ru.otus.domain.BookComment;
import ru.otus.domain.Genre;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@DataMongoTest
@DisplayName("mongo repository for book comments")
class BookCommentsRepositoryUnitTest {

	private static final String BOOK_NAME = "Architects of Memory";
	private static final String AUTHOR_NAME = "Karen";
	private static final String GENRE_NAME = "Fantasy";

	@Autowired
	private BookCommentsRepository commentsRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Test
	@DisplayName("should delete all comments by book id")
	@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
	public void shouldDeleteAllByBookId() {
		var book = mongoTemplate.findOne(query(where("name").is(BOOK_NAME)), Book.class);
		var comments = mongoTemplate.find(
				query(where("book.name").is(BOOK_NAME)),
				BookComment.class
		);

		assertThat(comments).isNotEmpty();
		assertThat(book).isNotNull();

		commentsRepository.deleteAllByBook_Id(book.getId()).block();

		var deletedComments = mongoTemplate.find(
				query(where("book.name").is(BOOK_NAME)),
				BookComment.class
		);

		assertThat(deletedComments).isEmpty();
	}

	@Test
	@DisplayName("should delete all comments by book author id")
	@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
	public void shouldDeleteAllByBookAuthorId() {
		var author = mongoTemplate.findOne(query(where("name").is(AUTHOR_NAME)), Author.class);
		var comments = mongoTemplate.find(
				query(where("book.author.name").is(AUTHOR_NAME)),
				BookComment.class
		);

		assertThat(comments).isNotEmpty();
		assertThat(author).isNotNull();

		commentsRepository.deleteAllByBook_Author_Id(author.getId()).block();

		var deletedComments = mongoTemplate.find(
				query(where("book.author.name").is(AUTHOR_NAME)),
				BookComment.class
		);

		assertThat(deletedComments).isEmpty();
	}

	@Test
	@DisplayName("should delete all comments by book genre id")
	@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
	public void shouldDeleteAllByBookGenreId() {
		var genre = mongoTemplate.findOne(
				query(where("name").is(GENRE_NAME)), Genre.class
		);
		var comments = mongoTemplate.find(
				query(where("book.genre.name").is(GENRE_NAME)),
				BookComment.class
		);

		assertThat(comments).isNotEmpty();
		assertThat(genre).isNotNull();

		commentsRepository.deleteAllByBook_Genre_Id(genre.getId()).block();

		var deletedComments = mongoTemplate.find(
				query(where("book.genre.name").is(GENRE_NAME)),
				BookComment.class
		);

		assertThat(deletedComments).isEmpty();
	}

	@Test
	@DisplayName("should return all comments by book id")
	public void shouldReturnAllCommentsByBookId() {
		var book = mongoTemplate.findOne(
				query(where("name").is(BOOK_NAME)), Book.class
		);

		assertThat(book).isNotNull();

		var bookId = book.getId();
		var expectedComments = mongoTemplate.find(
				query(where("book.id").is(bookId)),
				BookComment.class
		);

		assertThat(expectedComments).isNotNull().isNotEmpty();

		var comments = commentsRepository.findAllByBook_Id(bookId).toIterable();

		assertThat(comments).isNotEmpty();
		assertThat(comments)
				.usingElementComparatorIgnoringFields("book")
				.containsOnlyOnceElementsOf(expectedComments);
	}

	@Test
	@DisplayName("should return all comments by books ids")
	public void shouldReturnAllCommentsByBooksIds() {
		var booksIds = mongoTemplate.findAll(Book.class)
				.stream()
				.map(Book::getId)
				.collect(toList());

		assertThat(booksIds).isNotEmpty();

		var expectedComments = mongoTemplate.find(
				query(where("book.id").in(booksIds)),
				BookComment.class
		);

		assertThat(expectedComments).isNotEmpty();

		var comments = commentsRepository.findAllByBook_IdIn(booksIds).toIterable();

		assertThat(comments).isNotEmpty();
		assertThat(comments)
				.usingElementComparatorIgnoringFields("book")
				.containsOnlyOnceElementsOf(expectedComments);
	}
}