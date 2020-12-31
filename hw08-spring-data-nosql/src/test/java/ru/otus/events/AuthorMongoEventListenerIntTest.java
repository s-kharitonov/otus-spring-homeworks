package ru.otus.events;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.domain.Book;
import ru.otus.services.AuthorsService;
import ru.otus.services.BooksService;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("Mongo DB author event listener")
class AuthorMongoEventListenerIntTest {

	private static final String NAME = "sergey";

	@Autowired
	private BooksService booksService;

	@Autowired
	private AuthorsService authorsService;

	@Test
	@DisplayName("should delete related books after delete author")
	@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
	public void shouldDeleteRelatedBooksAfterDeleteAuthor() {
		var author = authorsService.getAll().get(0);

		assertThat(author).isNotNull();

		var authorId = author.getId();
		var booksForDelete = booksService.getByAuthorId(authorId);

		assertThat(booksForDelete).isNotEmpty();

		authorsService.deleteById(authorId);

		var deletedBooks = booksService.getByAuthorId(authorId);

		assertThat(deletedBooks).isEmpty();
	}

	@Test
	@DisplayName("should update author in related books after save author")
	@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
	public void shouldUpdateAuthorInRelatedBooksAfterSave() {
		var author = authorsService.getAll().get(0);

		assertThat(author).isNotNull();

		var authorId = author.getId();
		var booksForUpdate = booksService.getByAuthorId(authorId);

		assertThat(booksForUpdate).isNotEmpty();
		author.setName(NAME);

		authorsService.save(author);

		var updatedAuthorsInBooks = booksService.getByAuthorId(authorId).stream()
				.map(Book::getAuthor).collect(toList());

		assertThat(updatedAuthorsInBooks).isNotEmpty()
				.usingFieldByFieldElementComparator()
				.containsOnly(author);
	}
}