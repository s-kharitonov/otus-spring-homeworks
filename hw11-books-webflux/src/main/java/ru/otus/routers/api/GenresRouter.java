package ru.otus.routers.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.otus.domain.ErrorResponse;
import ru.otus.domain.Genre;
import ru.otus.exceptions.BadRequestException;
import ru.otus.repositories.GenresRepository;
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
public class GenresRouter {

	@Bean
	public RouterFunction<ServerResponse> genresRoute(final FieldValidator validator,
													  final GenresRepository genresRepository) {
		final var handler = new GenresHandler(validator, genresRepository);

		return route()
				.POST("/api/genre/", accept(APPLICATION_JSON), handler::create)
				.GET("/api/genre/", accept(APPLICATION_JSON), handler::getAll)
				.GET("/api/genre/{id}", accept(APPLICATION_JSON), handler::getById)
				.PUT("/api/genre/", accept(APPLICATION_JSON), handler::update)
				.DELETE("/api/genre/{id}", accept(APPLICATION_JSON), handler::delete)
				.onError(RuntimeException.class,
						(error, request) -> status(INTERNAL_SERVER_ERROR)
								.bodyValue(new ErrorResponse(INTERNAL_SERVER_ERROR.value(), error.getMessage()))
				)
				.build();
	}

	private static class GenresHandler {
		private final FieldValidator validator;
		private final GenresRepository genresRepository;

		private GenresHandler(final FieldValidator validator, final GenresRepository genresRepository) {
			this.validator = validator;
			this.genresRepository = genresRepository;
		}

		@Nonnull
		public Mono<ServerResponse> create(final ServerRequest request) {
			return request.bodyToMono(Genre.class)
					.doOnNext(this::checkGenre)
					.flatMap(genresRepository::save)
					.flatMap(genre -> created(request.uri()).contentType(APPLICATION_JSON).bodyValue(genre))
					.onErrorResume(BadRequestException.class,
							error -> badRequest()
									.contentType(APPLICATION_JSON)
									.bodyValue(new ErrorResponse(BAD_REQUEST.value(), error.getMessage()))
					);
		}

		@Nonnull
		public Mono<ServerResponse> getAll(final ServerRequest request) {
			var genres = genresRepository.findAll()
					.sort(Comparator.comparing(Genre::getName));

			return ok()
					.contentType(APPLICATION_JSON)
					.body(genres, Genre.class);
		}

		@Nonnull
		public Mono<ServerResponse> getById(final ServerRequest request) {
			return genresRepository.findById(request.pathVariable("id"))
					.flatMap(genre -> ok().contentType(APPLICATION_JSON).bodyValue(genre))
					.switchIfEmpty(notFound().build());
		}

		@Nonnull
		public Mono<ServerResponse> update(final ServerRequest request) {
			return request.bodyToMono(Genre.class)
					.doOnNext(this::checkGenre)
					.flatMap(genresRepository::save)
					.flatMap(genre -> noContent().build())
					.onErrorResume(BadRequestException.class,
							error -> badRequest()
									.contentType(APPLICATION_JSON)
									.bodyValue(new ErrorResponse(BAD_REQUEST.value(), error.getMessage()))
					);
		}

		@Nonnull
		public Mono<ServerResponse> delete(final ServerRequest request) {
			return genresRepository.deleteById(request.pathVariable("id"))
					.then(noContent().build());
		}

		private void checkGenre(final Genre genre) {
			if (validator.validate(genre).hasErrors()) {
				throw new BadRequestException("invalid genre fields!");
			}
		}
	}
}
