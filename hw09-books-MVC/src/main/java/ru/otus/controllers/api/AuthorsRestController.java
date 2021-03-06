package ru.otus.controllers.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.domain.Author;
import ru.otus.domain.ErrorResponse;
import ru.otus.domain.dto.AuthorDto;
import ru.otus.exceptions.AuthorsServiceException;
import ru.otus.exceptions.NotFoundException;
import ru.otus.services.AuthorsService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AuthorsRestController {

	private final AuthorsService authorsService;

	public AuthorsRestController(final AuthorsService authorsService) {
		this.authorsService = authorsService;
	}

	@PostMapping(value = "/api/author/")
	public ResponseEntity<AuthorDto> create(@RequestBody Author author) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new AuthorDto(authorsService.save(author)));
	}

	@GetMapping(value = "/api/author/{id}")
	public ResponseEntity<AuthorDto> getById(@PathVariable long id) {
		return ResponseEntity.ok(
				authorsService.getById(id)
						.map(AuthorDto::new)
						.orElseThrow(() -> new NotFoundException(
								String.format("Author with id: %s, not found!", id)
						))
		);
	}

	@GetMapping(value = "/api/author/")
	public ResponseEntity<List<AuthorDto>> getAll() {
		return ResponseEntity.ok(
				authorsService.getAll().stream()
						.map(AuthorDto::new)
						.collect(Collectors.toList())
		);
	}

	@DeleteMapping(value = "/api/author/{id}")
	public ResponseEntity<?> remove(@PathVariable long id) {
		authorsService.deleteById(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@PutMapping(value = "/api/author/")
	public ResponseEntity<?> update(@RequestBody Author author) {
		authorsService.save(author);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@ExceptionHandler(AuthorsServiceException.class)
	public ResponseEntity<ErrorResponse> serviceExceptionHandler(AuthorsServiceException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
	}
}
