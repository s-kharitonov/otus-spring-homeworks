package ru.otus.controllers.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.domain.Author;
import ru.otus.domain.dto.AuthorDto;
import ru.otus.exceptions.NotFoundException;
import ru.otus.services.AuthorsService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/author")
public class AuthorsRestController {

	private final AuthorsService authorsService;

	public AuthorsRestController(final AuthorsService authorsService) {
		this.authorsService = authorsService;
	}

	@PostMapping(value = "/")
	public ResponseEntity<AuthorDto> create(@RequestBody Author author) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new AuthorDto(authorsService.save(author)));
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<AuthorDto> getById(@PathVariable long id) {
		return ResponseEntity.ok(
				authorsService.getById(id)
						.map(AuthorDto::new)
						.orElseThrow(() -> new NotFoundException(
								String.format("Author with id: %s, not found!", id)
						))
		);
	}

	@GetMapping(value = "/")
	public ResponseEntity<List<AuthorDto>> getAll() {
		return ResponseEntity.ok(
				authorsService.getAll().stream()
						.map(AuthorDto::new)
						.collect(Collectors.toList())
		);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> remove(@PathVariable long id) {
		authorsService.deleteById(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@PutMapping(value = "/")
	public ResponseEntity<?> update(@RequestBody Author author) {
		authorsService.save(author);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
