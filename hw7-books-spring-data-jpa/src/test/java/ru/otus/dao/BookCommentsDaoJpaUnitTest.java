package ru.otus.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.domain.BookComment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(BookCommentsDaoJpa.class)
@DisplayName("DAO for work with comments")
class BookCommentsDaoJpaUnitTest {

	private static final String COMMENT = "perfect book!";
	private static final long COMMENT_ID = 1L;
	private static final String NEW_COMMENT = "nice book!";

	@Autowired
	private BookCommentsDao bookCommentsDao;

	@Autowired
	private TestEntityManager em;

	@Test
	@DisplayName("should save comment")
	public void shouldSaveComment() {
		var comment = new BookComment.Builder().text(COMMENT).build();

		bookCommentsDao.save(comment);
		assertNotNull(comment.getId());

		var savedComment = em.find(BookComment.class, comment.getId());

		assertThat(savedComment).isNotNull().isEqualToComparingFieldByField(comment);
	}

	@Test
	@DisplayName("should update comment")
	public void shouldUpdateComment() {
		var comment = em.find(BookComment.class, COMMENT_ID);

		comment.setText(NEW_COMMENT);
		em.detach(comment);
		bookCommentsDao.save(comment);

		var updatedComment = em.find(BookComment.class, COMMENT_ID);

		assertThat(comment).isEqualToIgnoringGivenFields(updatedComment, "book");
	}

	@Test
	@DisplayName("should return comment by id")
	public void shouldReturnCommentById() {
		var comment = bookCommentsDao.findById(COMMENT_ID).orElseThrow();
		var expectedComment = em.find(BookComment.class, COMMENT_ID);

		assertThat(comment).isEqualToComparingFieldByField(expectedComment);
	}

	@Test
	@DisplayName("should return all comments")
	public void shouldReturnAllComments() {
		var comments = bookCommentsDao.findAll();
		var expectedComments = em.getEntityManager()
				.createQuery("select c from BookComment c", BookComment.class)
				.getResultList();

		assertThat(comments).containsOnlyOnceElementsOf(expectedComments);
	}

	@Test
	@DisplayName("should remove comment by id")
	public void shouldRemoveCommentById() {
		var comment = em.find(BookComment.class, COMMENT_ID);

		assertNotNull(comment);
		em.detach(comment);
		assertTrue(bookCommentsDao.removeById(COMMENT_ID));

		var removedComment = em.find(BookComment.class, COMMENT_ID);

		assertNull(removedComment);
	}
}