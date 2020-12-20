package ru.otus.services.facades;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.domain.Book;
import ru.otus.domain.BookCandidate;
import ru.otus.exeptions.BooksServiceException;
import ru.otus.services.AuthorsService;
import ru.otus.services.BooksService;
import ru.otus.services.GenresService;

import java.util.List;
import java.util.Optional;

@Service
public class BooksFacadeImpl implements BooksFacade{

	private final BooksService booksService;
	private final GenresService genresService;
	private final AuthorsService authorsService;

	public BooksFacadeImpl(final BooksService booksService,
						   final GenresService genresService,
						   final AuthorsService authorsService) {
		this.booksService = booksService;
		this.genresService = genresService;
		this.authorsService = authorsService;
	}

	@Override
	@Transactional
	public Book save(final BookCandidate bookCandidate) {
		final var author = authorsService.getById(bookCandidate.getAuthorId())
				.orElseThrow(() -> new BooksServiceException("author not found!"));
		final var genre = genresService.getById(bookCandidate.getGenreId())
				.orElseThrow(() -> new BooksServiceException("genre not found!"));
		final var book = new Book.Builder()
				.id(bookCandidate.getBookId())
				.name(bookCandidate.getName())
				.printLength(bookCandidate.getPrintLength())
				.publicationDate(bookCandidate.getPublicationDate())
				.author(author)
				.genre(genre)
				.build();

		return booksService.save(book);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Book> getById(final String id) {
		return booksService.getById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Book> getAll() {
		return booksService.getAll();
	}

	@Override
	@Transactional
	public void deleteById(final String id) {
		booksService.deleteById(id);
	}
}
