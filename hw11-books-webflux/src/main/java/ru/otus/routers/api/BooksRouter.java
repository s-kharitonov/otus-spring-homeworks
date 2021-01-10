package ru.otus.routers.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.otus.domain.Book;
import ru.otus.domain.BookCandidate;
import ru.otus.domain.ErrorResponse;
import ru.otus.exceptions.BadRequestException;
import ru.otus.repositories.AuthorsRepository;
import ru.otus.repositories.BooksRepository;
import ru.otus.repositories.GenresRepository;
import ru.otus.validators.FieldValidator;

import javax.annotation.Nonnull;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.*;

@Configuration
public class BooksRouter {

	@Bean
	public RouterFunction<ServerResponse> booksRoute(final FieldValidator validator,
													 final BooksRepository booksRepository,
													 final AuthorsRepository authorsRepository,
													 final GenresRepository genresRepository) {
		final var handler = new BooksHandler(validator, booksRepository, authorsRepository, genresRepository);

		return route()
				.POST("/api/book/", accept(APPLICATION_JSON), handler::create)
				.GET("/api/book/", accept(APPLICATION_JSON), handler::getAll)
				.GET("/api/book/{id}", accept(APPLICATION_JSON), handler::getById)
				.PUT("/api/book/", accept(APPLICATION_JSON), handler::update)
				.DELETE("/api/book/{id}", accept(APPLICATION_JSON), handler::delete)
				.onError(RuntimeException.class,
						(error, request) -> status(INTERNAL_SERVER_ERROR)
								.bodyValue(new ErrorResponse(INTERNAL_SERVER_ERROR.value(), error.getMessage()))
				)
				.build();
	}

	private static class BooksHandler {
		private final FieldValidator validator;
		private final BooksRepository booksRepository;
		private final AuthorsRepository authorsRepository;
		private final GenresRepository genresRepository;

		private BooksHandler(final FieldValidator validator,
							 final BooksRepository booksRepository,
							 final AuthorsRepository authorsRepository,
							 final GenresRepository genresRepository) {
			this.validator = validator;
			this.booksRepository = booksRepository;
			this.authorsRepository = authorsRepository;
			this.genresRepository = genresRepository;
		}

		@Nonnull
		public Mono<ServerResponse> create(final ServerRequest request) {
			return request.bodyToMono(BookCandidate.class)
					.flatMap(this::buildBook)
					.doOnNext(this::checkBook)
					.flatMap(booksRepository::save)
					.flatMap(book -> created(request.uri()).contentType(APPLICATION_JSON).bodyValue(book))
					.onErrorResume(BadRequestException.class,
							error -> badRequest()
									.contentType(APPLICATION_JSON)
									.bodyValue(new ErrorResponse(BAD_REQUEST.value(), error.getMessage()))
					);
		}

		@Nonnull
		public Mono<ServerResponse> getAll(final ServerRequest request) {
			return ok()
					.contentType(APPLICATION_JSON)
					.body(booksRepository.findAll(), Book.class);
		}

		@Nonnull
		public Mono<ServerResponse> getById(final ServerRequest request) {
			return booksRepository.findById(request.pathVariable("id"))
					.flatMap(book -> ok().contentType(APPLICATION_JSON).bodyValue(book))
					.switchIfEmpty(notFound().build());
		}

		@Nonnull
		public Mono<ServerResponse> update(final ServerRequest request) {
			return request.bodyToMono(Book.class)
					.doOnNext(this::checkBook)
					.flatMap(booksRepository::save)
					.flatMap(author -> noContent().build())
					.onErrorResume(BadRequestException.class,
							error -> badRequest()
									.contentType(APPLICATION_JSON)
									.bodyValue(new ErrorResponse(BAD_REQUEST.value(), error.getMessage()))
					);
		}

		@Nonnull
		public Mono<ServerResponse> delete(final ServerRequest request) {
			return booksRepository.deleteById(request.pathVariable("id"))
					.then(noContent().build());
		}

		private Mono<Book> buildBook(final BookCandidate bookCandidate) {
			final var authorId = bookCandidate.getAuthorId();
			final var genreId = bookCandidate.getGenreId();

			return authorsRepository.findById(authorId)
					.zipWith(genresRepository.findById(genreId))
					.map(tuple -> {
						var author = tuple.getT1();
						var genre = tuple.getT2();

						return new Book.Builder()
								.name(bookCandidate.getName())
								.publicationDate(bookCandidate.getPublicationDate())
								.printLength(bookCandidate.getPrintLength())
								.genre(genre)
								.author(author)
								.build();
					})
					.switchIfEmpty(Mono.just(
							new Book.Builder()
									.name(bookCandidate.getName())
									.publicationDate(bookCandidate.getPublicationDate())
									.printLength(bookCandidate.getPrintLength())
									.genre(null)
									.author(null)
									.build()
					));
		}

		private void checkBook(final Book book) {
			if (validator.validate(book).hasErrors()) {
				throw new BadRequestException("invalid book fields!");
			}
		}
	}
}
