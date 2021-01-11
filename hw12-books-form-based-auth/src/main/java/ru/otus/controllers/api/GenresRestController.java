package ru.otus.controllers.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.domain.ErrorResponse;
import ru.otus.domain.Genre;
import ru.otus.domain.dto.GenreDto;
import ru.otus.exceptions.GenresServiceException;
import ru.otus.exceptions.NotFoundException;
import ru.otus.services.GenresService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class GenresRestController {

	private final GenresService genresService;

	public GenresRestController(final GenresService genresService) {
		this.genresService = genresService;
	}

	@PostMapping(value = "/api/genre/")
	public ResponseEntity<GenreDto> create(@RequestBody Genre genre) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new GenreDto(genresService.save(genre)));
	}

	@GetMapping(value = "/api/genre/{id}")
	public ResponseEntity<GenreDto> getById(@PathVariable long id) {
		return ResponseEntity.ok(
				genresService.getById(id)
						.map(GenreDto::new)
						.orElseThrow(() -> new NotFoundException(
								String.format("Genre with id: %s, not found!", id)
						)));
	}

	@GetMapping(value = "/api/genre/")
	public ResponseEntity<List<GenreDto>> getAll() {
		return ResponseEntity.ok(
				genresService.getAll().stream()
						.map(GenreDto::new)
						.sorted(Comparator.comparing(GenreDto::getName))
						.collect(Collectors.toUnmodifiableList())
		);
	}

	@DeleteMapping(value = "/api/genre/{id}")
	public ResponseEntity<?> delete(@PathVariable long id) {
		genresService.deleteById(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@PutMapping(value = "/api/genre/")
	public ResponseEntity<?> update(@RequestBody Genre genre) {
		genresService.save(genre);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@ExceptionHandler(GenresServiceException.class)
	public ResponseEntity<ErrorResponse> serviceExceptionHandler(GenresServiceException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
	}
}
