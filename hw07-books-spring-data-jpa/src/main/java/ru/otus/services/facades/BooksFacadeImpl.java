package ru.otus.services.facades;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.domain.Book;
import ru.otus.domain.dto.BookDto;
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
	public BookDto save(final BookDto book) {
		var author = authorsService.getById(book.getAuthor().getId()).orElseThrow();
		var genre = genresService.getById(book.getGenre().getId()).orElseThrow();
		var bookForSave = new Book.Builder()
				.id(book.getId())
				.name(book.getName())
				.publicationDate(book.getPublicationDate())
				.printLength(book.getPrintLength())
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
}
