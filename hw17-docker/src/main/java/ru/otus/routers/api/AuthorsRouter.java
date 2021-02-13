package ru.otus.routers.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.otus.domain.Author;
import ru.otus.domain.AuthorDto;
import ru.otus.domain.ErrorResponse;
import ru.otus.exceptions.BadRequestException;
import ru.otus.repositories.AuthorsRepository;
import ru.otus.validators.FieldValidator;

import javax.annotation.Nonnull;
import java.util.Comparator;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.*;

@Configuration
public class AuthorsRouter {

	@Bean
	public RouterFunction<ServerResponse> authorsRoute(final FieldValidator validator,
													   final AuthorsRepository authorsRepository) {
		final var handler = new AuthorsHandler(validator, authorsRepository);

		return route()
				.POST("/api/author/", accept(APPLICATION_JSON), handler::create)
				.GET("/api/author/", accept(APPLICATION_JSON), handler::getAll)
				.GET("/api/author/{id}", accept(APPLICATION_JSON), handler::getById)
				.PUT("/api/author/", accept(APPLICATION_JSON), handler::update)
				.DELETE("/api/author/{id}", accept(APPLICATION_JSON), handler::delete)
				.onError(RuntimeException.class,
						(error, request) -> status(INTERNAL_SERVER_ERROR)
								.bodyValue(new ErrorResponse(INTERNAL_SERVER_ERROR.value(), error.getMessage()))
				)
				.build();
	}

	private static class AuthorsHandler {
		private final FieldValidator validator;
		private final AuthorsRepository authorsRepository;

		private AuthorsHandler(final FieldValidator validator, final AuthorsRepository authorsRepository) {
			this.validator = validator;
			this.authorsRepository = authorsRepository;
		}

		@Nonnull
		public Mono<ServerResponse> create(final ServerRequest request) {
			return request.bodyToMono(Author.class)
					.doOnNext(this::checkAuthor)
					.flatMap(authorsRepository::save)
					.flatMap(author -> created(request.uri()).contentType(APPLICATION_JSON).bodyValue(new AuthorDto(author)))
					.onErrorResume(BadRequestException.class,
							error -> badRequest()
									.contentType(APPLICATION_JSON)
									.bodyValue(new ErrorResponse(BAD_REQUEST.value(), error.getMessage()))
					);
		}

		@Nonnull
		public Mono<ServerResponse> getAll(final ServerRequest request) {
			var authors = authorsRepository.findAll()
					.map(AuthorDto::new)
					.sort(Comparator.comparing(AuthorDto::getFullName));

			return ok()
					.contentType(APPLICATION_JSON)
					.body(authors, AuthorDto.class);
		}

		@Nonnull
		public Mono<ServerResponse> getById(final ServerRequest request) {
			return authorsRepository.findById(request.pathVariable("id"))
					.flatMap(author -> ok().contentType(APPLICATION_JSON).bodyValue(new AuthorDto(author)))
					.switchIfEmpty(notFound().build());
		}

		@Nonnull
		public Mono<ServerResponse> update(final ServerRequest request) {
			return request.bodyToMono(Author.class)
					.doOnNext(this::checkAuthor)
					.flatMap(authorsRepository::save)
					.flatMap(author -> noContent().build())
					.onErrorResume(BadRequestException.class,
							error -> badRequest()
									.contentType(APPLICATION_JSON)
									.bodyValue(new ErrorResponse(BAD_REQUEST.value(), error.getMessage()))
					);
		}

		@Nonnull
		public Mono<ServerResponse> delete(final ServerRequest request) {
			return authorsRepository.deleteById(request.pathVariable("id"))
					.then(noContent().build());
		}

		private void checkAuthor(final Author author) {
			if (validator.validate(author).hasErrors()) {
				throw new BadRequestException("invalid author fields!");
			}
		}
	}
}
