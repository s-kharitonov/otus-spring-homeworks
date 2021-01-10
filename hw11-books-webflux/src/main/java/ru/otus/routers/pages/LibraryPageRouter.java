package ru.otus.routers.pages;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.TEXT_HTML;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class LibraryPageRouter {

	@Bean
	public RouterFunction<ServerResponse> libraryPageRoute() {
		return route()
				.GET("/library", accept(TEXT_HTML), request -> ok().contentType(TEXT_HTML).render("library"))
				.build();
	}
}
