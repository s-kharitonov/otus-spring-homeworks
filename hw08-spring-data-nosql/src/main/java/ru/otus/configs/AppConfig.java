package ru.otus.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.strategies.DeleteBookByAuthorStrategy;
import ru.otus.strategies.DeleteBookByGenreStrategy;
import ru.otus.strategies.DeleteCommentByBookStrategy;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

@Configuration
public class AppConfig {

	@Bean
	public Map<String, DeleteCommentByBookStrategy> deleteCommentByBookStrategy(final List<DeleteCommentByBookStrategy> strategies) {
		return strategies.stream()
				.collect(toMap(DeleteCommentByBookStrategy::getFieldName, Function.identity()));
	}

	@Bean
	public Map<String, DeleteBookByAuthorStrategy> deleteBookByAuthorStrategy(final List<DeleteBookByAuthorStrategy> strategies) {
		return strategies.stream()
				.collect(toMap(DeleteBookByAuthorStrategy::getFieldName, Function.identity()));
	}

	@Bean
	public Map<String, DeleteBookByGenreStrategy> deleteBookByGenreStrategy(final List<DeleteBookByGenreStrategy> strategies) {
		return strategies.stream()
				.collect(toMap(DeleteBookByGenreStrategy::getFieldName, Function.identity()));
	}
}
