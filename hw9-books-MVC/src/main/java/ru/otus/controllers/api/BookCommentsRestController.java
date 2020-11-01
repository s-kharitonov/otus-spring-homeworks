package ru.otus.controllers.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.domain.BookComment;
import ru.otus.domain.BookCommentCandidate;
import ru.otus.domain.ErrorResponse;
import ru.otus.domain.dto.BookCommentDto;
import ru.otus.exceptions.BookCommentsServiceException;
import ru.otus.exceptions.NotFoundException;
import ru.otus.services.facades.BookCommentsFacade;

import java.util.List;

@RestController
@RequestMapping("/book/comment")
public class BookCommentsRestController {

	private final BookCommentsFacade bookCommentsFacade;

	public BookCommentsRestController(final BookCommentsFacade bookCommentsFacade) {
		this.bookCommentsFacade = bookCommentsFacade;
	}

	@PostMapping(value = "/")
	public ResponseEntity<BookCommentDto> create(@RequestBody BookCommentCandidate candidate) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(bookCommentsFacade.create(candidate));
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<BookCommentDto> getById(@PathVariable long id) {
		return ResponseEntity.ok(
				bookCommentsFacade.getById(id).orElseThrow(() -> new NotFoundException(
						String.format("BookComment with id: %s, not found!", id)
				))
		);
	}

	@GetMapping(value = "/")
	public ResponseEntity<List<BookCommentDto>> getAll() {
		return ResponseEntity.ok(bookCommentsFacade.getAll());
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> deleteById(@PathVariable long id) {
		bookCommentsFacade.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@PutMapping(value = "/")
	public ResponseEntity<?> update(@RequestBody BookComment bookComment) {
		bookCommentsFacade.update(bookComment);
		return ResponseEntity.noContent().build();
	}

	@ExceptionHandler(BookCommentsServiceException.class)
	public ResponseEntity<ErrorResponse> serviceExceptionHandler(BookCommentsServiceException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
	}
}
