package ru.otus.controllers.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.domain.Book;
import ru.otus.domain.BookCandidate;
import ru.otus.domain.ErrorResponse;
import ru.otus.domain.dto.BookDto;
import ru.otus.exceptions.BooksServiceException;
import ru.otus.exceptions.NotFoundException;
import ru.otus.services.facades.BooksFacade;

import java.util.List;

@RestController
public class BooksRestController {

	private final BooksFacade booksFacade;

	public BooksRestController(final BooksFacade booksFacade) {
		this.booksFacade = booksFacade;
	}

	@PostMapping(value = "/api/book/")
	public ResponseEntity<BookDto> create(@RequestBody BookCandidate candidate) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(booksFacade.create(candidate));
	}

	@GetMapping(value = "/api/book/{id}")
	public ResponseEntity<BookDto> getBookById(@PathVariable long id) {
		return ResponseEntity.ok(
				booksFacade.getById(id)
						.orElseThrow(() -> new NotFoundException(
								String.format("Book with id: %s, not found!", id)
						))
		);
	}

	@GetMapping(value = "/api/book/")
	public ResponseEntity<List<BookDto>> getAllBooks() {
		return ResponseEntity.ok(booksFacade.getAll());
	}

	@DeleteMapping(value = "/api/book/{id}")
	public ResponseEntity<?> removeBook(@PathVariable long id) {
		booksFacade.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@PutMapping(value = "/api/book/")
	public ResponseEntity<?> update(@RequestBody Book book) {
		booksFacade.update(book);
		return ResponseEntity.noContent().build();
	}

	@ExceptionHandler(BooksServiceException.class)
	public ResponseEntity<ErrorResponse> serviceExceptionHandler(BooksServiceException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
	}
}
