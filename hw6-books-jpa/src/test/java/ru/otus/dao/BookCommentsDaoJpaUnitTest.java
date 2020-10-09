package ru.otus.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.domain.Book;
import ru.otus.domain.BookComment;
import ru.otus.domain.Comment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@Import(BookCommentsDaoJpa.class)
@DisplayName("DAO for work with book comments")
class BookCommentsDaoJpaUnitTest {

	private static final long BOOK_ID = 1L;
	private static final long COMMENT_ID = 1L;
	private static final long BOOK_COMMENT_ID = 1L;
	private static final long SECOND_BOOK_ID = 2L;

	@Autowired
	private BookCommentsDao bookCommentsDao;

	@Autowired
	private TestEntityManager em;

	@Test
	@DisplayName("should save book comment")
	public void shouldSaveBookComment() {
		var book = em.find(Book.class, BOOK_ID);
		var comment = em.find(Comment.class, COMMENT_ID);

		assertNotNull(book);
		assertNotNull(comment);

		var bookComment = new BookComment(book, comment);

		bookCommentsDao.save(bookComment);

		assertNotNull(bookComment.getId());

		var createdBookComment = em.find(BookComment.class, bookComment.getId());

		assertThat(createdBookComment).isNotNull().isEqualToComparingFieldByField(bookComment);
	}

	@Test
	@DisplayName("should update book comment")
	public void shouldUpdateBookComment() {
		var bookComment = em.find(BookComment.class, BOOK_COMMENT_ID);

		assertNotNull(bookComment);

		var book = em.find(Book.class, SECOND_BOOK_ID);

		assertNotNull(book);
		bookComment.setBook(book);
		em.detach(bookComment);
		bookCommentsDao.save(bookComment);

		var updatedBookComment = em.find(BookComment.class, BOOK_COMMENT_ID);

		assertNotNull(updatedBookComment);
		assertThat(book).isEqualToIgnoringGivenFields(
				updatedBookComment.getBook(),
				"comments", "author", "genre"
		);
	}
}