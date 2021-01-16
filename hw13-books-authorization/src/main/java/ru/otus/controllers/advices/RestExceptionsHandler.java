package ru.otus.controllers.advices;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.otus.domain.ErrorResponse;
import ru.otus.exceptions.NotFoundException;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class RestExceptionsHandler {

	@ExceptionHandler(NotFoundException.class)
	private ResponseEntity<ErrorResponse> handleNotFound(NotFoundException e) {
		return ResponseEntity.status(NOT_FOUND)
				.body(new ErrorResponse(NOT_FOUND.value(), e.getMessage()));
	}

	@ExceptionHandler(AccessDeniedException.class)
	private ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException e) {
		return ResponseEntity.status(FORBIDDEN)
				.body(new ErrorResponse(FORBIDDEN.value(), e.getMessage()));
	}
}
