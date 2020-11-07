package ru.otus.services.facades;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.domain.Book;
import ru.otus.domain.BookCandidate;
import ru.otus.domain.dto.BookDto;
import ru.otus.exceptions.BooksServiceException;
import ru.otus.services.AuthorsService;
import ru.otus.services.BooksService;
import ru.otus.services.GenresService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BooksFacadeImpl implements BooksFacade {

	private final BooksService booksService;
	private final AuthorsService authorsService;
	private final GenresService genresService;

	public BooksFacadeImpl(final BooksService booksService,
						   final AuthorsService authorsService,
						   final GenresService genresService) {
		this.booksService = booksService;
		this.authorsService = authorsService;
		this.genresService = genresService;
	}

	@Override
	@Transactional
	public BookDto create(final BookCandidate candidate) {
		final long authorId = candidate.getAuthorId();
		final long genreId = candidate.getGenreId();
		var author = authorsService.getById(authorId)
				.orElseThrow(() -> new BooksServiceException(
						String.format("Author with id: %s, not found!", authorId)
				));
		var genre = genresService.getById(genreId)
				.orElseThrow(() -> new BooksServiceException(
						String.format("Genre with id: %s, not found!", genreId)
				));
		var bookForSave = new Book.Builder()
				.name(candidate.getName())
				.publicationDate(candidate.getPublicationDate())
				.printLength(candidate.getPrintLength())
				.author(author)
				.genre(genre)
				.build();

		return new BookDto(booksService.save(bookForSave));
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<BookDto> getById(final long id) {
		return booksService.getById(id).map(BookDto::new);
	}

	@Override
	@Transactional(readOnly = true)
	public List<BookDto> getAll() {
		return booksService.getAll()
				.stream()
				.map(BookDto::new)
				.collect(Collectors.toUnmodifiableList());
	}

	@Override
	@Transactional
	public void deleteById(final long id) {
		booksService.deleteById(id);
	}

	@Override
	@Transactional
	public void update(final Book book) {
		booksService.save(book);
	}
}
