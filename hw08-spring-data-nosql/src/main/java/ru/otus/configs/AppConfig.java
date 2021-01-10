package ru.otus.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.strategies.DeleteEntityStrategy;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

@Configuration
public class AppConfig {

	@Bean
	public Map<String, DeleteEntityStrategy> deleteEntityStrategies(final List<DeleteEntityStrategy> strategies) {
		return strategies.stream()
				.collect(toMap(DeleteEntityStrategy::getCollectionFieldName, Function.identity()));
	}
}
