package ru.otus.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.domain.Book;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("repository for work with books")
class BooksRepositoryUnitTest {

	@Autowired
	private BooksRepository booksRepository;

	@Autowired
	private TestEntityManager em;

	@Test
	@DisplayName("should return all books")
	public void shouldReturnAllBooks() {
		var books = booksRepository.findAll();
		var expectedBooks = em.getEntityManager()
				.createQuery("select b from Book b join fetch b.author join fetch b.genre", Book.class)
				.getResultList();

		assertThat(books).containsOnlyOnceElementsOf(expectedBooks);
	}
}