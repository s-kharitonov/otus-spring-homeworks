package ru.otus.routers.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.otus.domain.BookComment;
import ru.otus.domain.BookCommentCandidate;
import ru.otus.domain.ErrorResponse;
import ru.otus.exceptions.BadRequestException;
import ru.otus.repositories.BookCommentsRepository;
import ru.otus.repositories.BooksRepository;
import ru.otus.validators.FieldValidator;

import javax.annotation.Nonnull;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.*;

@Configuration
public class BookCommentsRouter {

	@Bean
	public RouterFunction<ServerResponse> bookCommentsRoute(final FieldValidator validator,
															final BookCommentsRepository commentsRepository,
															final BooksRepository booksRepository) {
		final var handler = new BookCommentsHandler(validator, commentsRepository, booksRepository);

		return route()
				.POST("/api/book/comment/", accept(APPLICATION_JSON), handler::create)
				.GET("/api/book/comment/", accept(APPLICATION_JSON), handler::getAll)
				.GET("/api/book/comment/{id}", accept(APPLICATION_JSON), handler::getById)
				.PUT("/api/book/comment/", accept(APPLICATION_JSON), handler::update)
				.DELETE("/api/book/comment/{id}", accept(APPLICATION_JSON), handler::delete)
				.onError(RuntimeException.class,
						(error, request) -> status(INTERNAL_SERVER_ERROR)
								.bodyValue(new ErrorResponse(INTERNAL_SERVER_ERROR.value(), error.getMessage()))
				)
				.build();
	}

	private static class BookCommentsHandler {
		private final FieldValidator validator;
		private final BookCommentsRepository commentsRepository;
		private final BooksRepository booksRepository;

		private BookCommentsHandler(final FieldValidator validator,
									final BookCommentsRepository commentsRepository,
									final BooksRepository booksRepository) {
			this.validator = validator;
			this.commentsRepository = commentsRepository;
			this.booksRepository = booksRepository;
		}

		@Nonnull
		public Mono<ServerResponse> create(final ServerRequest request) {
			return request.bodyToMono(BookCommentCandidate.class)
					.flatMap(this::buildComment)
					.doOnNext(this::checkComment)
					.flatMap(commentsRepository::save)
					.flatMap(comment -> created(request.uri()).contentType(APPLICATION_JSON).bodyValue(comment))
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
					.body(commentsRepository.findAll(), BookComment.class);
		}

		@Nonnull
		public Mono<ServerResponse> getById(final ServerRequest request) {
			return commentsRepository.findById(request.pathVariable("id"))
					.flatMap(comment -> ok().contentType(APPLICATION_JSON).bodyValue(comment))
					.switchIfEmpty(notFound().build());
		}

		@Nonnull
		public Mono<ServerResponse> update(final ServerRequest request) {
			return request.bodyToMono(BookComment.class)
					.doOnNext(this::checkComment)
					.flatMap(commentsRepository::save)
					.flatMap(author -> noContent().build())
					.onErrorResume(BadRequestException.class,
							error -> badRequest()
									.contentType(APPLICATION_JSON)
									.bodyValue(new ErrorResponse(BAD_REQUEST.value(), error.getMessage()))
					);
		}

		@Nonnull
		public Mono<ServerResponse> delete(final ServerRequest request) {
			return commentsRepository.deleteById(request.pathVariable("id"))
					.then(noContent().build());
		}

		private Mono<BookComment> buildComment(final BookCommentCandidate commentCandidate) {
			return booksRepository.findById(commentCandidate.getBookId())
					.map(book -> new BookComment.Builder()
							.book(book)
							.text(commentCandidate.getText())
							.build()
					)
					.switchIfEmpty(
							Mono.just(new BookComment.Builder()
									.book(null)
									.text(commentCandidate.getText())
									.build()
							)
					);
		}

		private void checkComment(final BookComment comment) {
			if (validator.validate(comment).hasErrors()) {
				throw new BadRequestException("invalid book comment fields!");
			}
		}
	}
}
