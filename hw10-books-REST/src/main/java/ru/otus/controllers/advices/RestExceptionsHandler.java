package ru.otus.controllers.advices;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.otus.domain.ErrorResponse;
import ru.otus.exceptions.NotFoundException;

@RestControllerAdvice
public class RestExceptionsHandler {

	@ExceptionHandler(NotFoundException.class)
	private ResponseEntity<ErrorResponse> handleNotFound(NotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage()));
	}
}
