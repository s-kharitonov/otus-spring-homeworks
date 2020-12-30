package ru.otus.events;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.domain.Book;
import ru.otus.domain.BookComment;
import ru.otus.services.BookCommentsService;
import ru.otus.services.BooksService;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("Mongo DB books event listener")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BooksMongoEventListenerIntTest {

	private static final int PRINT_LENGTH = 500;

	@Autowired
	private BookCommentsService bookCommentsService;

	@Autowired
	private BooksService booksService;

	@Test
	@DisplayName("should update book in related comments after save books")
	public void shouldUpdateBookInRelatedCommentsAfterSaveBooks() {
		var books = booksService.getAll();

		assertThat(books).isNotEmpty();
		books.forEach(book -> book.setPrintLength(PRINT_LENGTH));
		booksService.saveAll(books);

		var booksIds = books.stream()
				.map(Book::getId)
				.collect(toList());
		var updatedBooks = bookCommentsService.getByBooksIds(booksIds).stream()
				.map(BookComment::getBook)
				.collect(toList());
		var book2HasComments = updatedBooks.stream()
				.collect(toMap(Book::getId, book -> true, (existing, replacement) -> existing));
		var booksWithComments = books.stream()
				.filter(book -> book2HasComments.getOrDefault(book.getId(), false))
				.collect(toList());

		assertThat(updatedBooks).isNotEmpty()
				.usingElementComparatorIgnoringFields("author", "genre")
				.containsAll(booksWithComments);
	}
}