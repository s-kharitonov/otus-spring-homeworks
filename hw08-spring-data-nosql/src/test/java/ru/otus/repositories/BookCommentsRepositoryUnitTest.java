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
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookCommentsRepositoryUnitTest {

	private static final String BOOK_NAME = "Architects of Memory";
	private static final String AUTHOR_NAME = "Karen";
	private static final String GENRE_NAME = "Fantasy";

	@Autowired
	private BookCommentsRepository bookCommentsRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Test
	@DisplayName("should delete all comments by book id")
	public void shouldDeleteAllByBookId() {
		var book = mongoTemplate.findOne(query(where("name").is(BOOK_NAME)), Book.class);
		var comments = mongoTemplate.find(
				query(where("book.name").is(BOOK_NAME)),
				BookComment.class
		);

		assertThat(comments).isNotEmpty();
		assertThat(book).isNotNull();

		bookCommentsRepository.deleteAllByBook_Id(book.getId());

		var deletedComments = mongoTemplate.find(
				query(where("book.name").is(BOOK_NAME)),
				BookComment.class
		);

		assertThat(deletedComments).isEmpty();
	}

	@Test
	@DisplayName("should delete all comments by book author id")
	public void shouldDeleteAllByBookAuthorId() {
		var author = mongoTemplate.findOne(query(where("name").is(AUTHOR_NAME)), Author.class);
		var comments = mongoTemplate.find(
				query(where("book.author.name").is(AUTHOR_NAME)),
				BookComment.class
		);

		assertThat(comments).isNotEmpty();
		assertThat(author).isNotNull();

		bookCommentsRepository.deleteAllByBook_Author_id(author.getId());

		var deletedComments = mongoTemplate.find(
				query(where("book.author.name").is(AUTHOR_NAME)),
				BookComment.class
		);

		assertThat(deletedComments).isEmpty();
	}

	@Test
	@DisplayName("should delete all comments by book genre id")
	public void shouldDeleteAllByBookGenreId() {
		var genre = mongoTemplate.findOne(query(where("name").is(GENRE_NAME)), Genre.class);
		var comments = mongoTemplate.find(
				query(where("book.genre.name").is(GENRE_NAME)),
				BookComment.class
		);

		assertThat(comments).isNotEmpty();
		assertThat(genre).isNotNull();

		bookCommentsRepository.deleteAllByBook_Genre_id(genre.getId());

		var deletedComments = mongoTemplate.find(
				query(where("book.genre.name").is(GENRE_NAME)),
				BookComment.class
		);

		assertThat(deletedComments).isEmpty();
	}

	@Test
	@DisplayName("should return all comments by book id")
	public void shouldReturnAllCommentsByBookId() {
		var book = mongoTemplate.findOne(query(where("name").is(BOOK_NAME)), Book.class);

		assertThat(book).isNotNull();

		var bookId = book.getId();
		var expectedComments = mongoTemplate.find(
				query(where("book.id").is(bookId)),
				BookComment.class
		);
		var comments = bookCommentsRepository.findAllByBook_Id(bookId);

		assertThat(expectedComments).isNotEmpty();
		assertThat(comments).isNotEmpty()
				.usingElementComparatorIgnoringFields("book")
				.containsOnlyOnceElementsOf(expectedComments);
	}


	@Test
	@DisplayName("should return all comments by books ids")
	public void shouldReturnAllCommentsByBooksIds() {
		var books = mongoTemplate.findAll(Book.class);
		var booksIds = books.stream()
				.map(Book::getId)
				.collect(toList());

		assertThat(books).isNotEmpty();
		assertThat(booksIds).isNotEmpty();

		var expectedComments = mongoTemplate.find(
				query(where("book.id").in(booksIds)),
				BookComment.class
		);
		var comments = bookCommentsRepository.findAllByBook_IdIn(booksIds);

		assertThat(expectedComments).isNotEmpty();
		assertThat(comments).usingElementComparatorIgnoringFields("book")
				.containsOnlyOnceElementsOf(expectedComments);
	}
}