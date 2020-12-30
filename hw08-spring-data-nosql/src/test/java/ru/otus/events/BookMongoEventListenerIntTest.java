package ru.otus.events;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.domain.Book;
import ru.otus.domain.BookComment;
import ru.otus.services.BookCommentsService;
import ru.otus.services.BooksService;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@SpringBootTest
@DisplayName("Mongo DB book event listener")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookMongoEventListenerIntTest {

	private static final String BOOK_NAME = "Architects of Memory";
	private static final int PRINT_LENGTH = 500;

	@Autowired
	private BookCommentsService bookCommentsService;

	@Autowired
	private BooksService booksService;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Test
	@DisplayName("should delete related book comments after delete book by book id")
	public void shouldDeleteRelatedBookCommentAfterDeleteBookByBookId() {
		var book = mongoTemplate.findOne(query(where("name").is(BOOK_NAME)), Book.class);

		assertThat(book).isNotNull();

		var bookId = book.getId();
		var commentsForDelete = bookCommentsService.getByBookId(bookId);

		assertThat(commentsForDelete).isNotEmpty();

		booksService.deleteById(bookId);

		var deletedComments = bookCommentsService.getByBookId(bookId);

		assertThat(deletedComments).isEmpty();
	}

	@Test
	@DisplayName("should delete related book comments after delete book by genre id")
	public void shouldDeleteRelatedBookCommentAfterDeleteBookByGenreId() {
		var book = mongoTemplate.findOne(query(where("name").is(BOOK_NAME)), Book.class);

		assertThat(book).isNotNull();

		var bookId = book.getId();
		var genreId = book.getGenre().getId();
		var commentsForDelete = bookCommentsService.getByBookId(bookId);

		assertThat(commentsForDelete).isNotEmpty();

		booksService.deleteByGenreId(genreId);

		var deletedComments = bookCommentsService.getByBookId(bookId);

		assertThat(deletedComments).isEmpty();
	}

	@Test
	@DisplayName("should delete related book comments after delete book by author id")
	public void shouldDeleteRelatedBookCommentAfterDeleteBookByAuthorId() {
		var book = mongoTemplate.findOne(query(where("name").is(BOOK_NAME)), Book.class);

		assertThat(book).isNotNull();

		var bookId = book.getId();
		var authorId = book.getAuthor().getId();
		var commentsForDelete = bookCommentsService.getByBookId(bookId);

		assertThat(commentsForDelete).isNotEmpty();

		booksService.deleteByAuthorId(authorId);

		var deletedComments = bookCommentsService.getByBookId(bookId);

		assertThat(deletedComments).isEmpty();
	}

	@Test
	@DisplayName("should update book in related comments after save book")
	public void shouldUpdateBookInRelatedCommentsAfterSaveBook() {
		var book = mongoTemplate.findOne(query(where("name").is(BOOK_NAME)), Book.class);

		assertThat(book).isNotNull();
		book.setPrintLength(PRINT_LENGTH);
		booksService.save(book);

		var bookId = book.getId();
		var updatedBooks = bookCommentsService.getByBookId(bookId).stream()
				.map(BookComment::getBook)
				.collect(toList());

		assertThat(updatedBooks).isNotEmpty()
				.usingElementComparatorIgnoringFields("author", "genre")
				.containsOnly(book);
	}
}