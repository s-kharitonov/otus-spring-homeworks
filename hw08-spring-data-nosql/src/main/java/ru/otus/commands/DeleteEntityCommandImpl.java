package ru.otus.commands;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.otus.exсeptions.NotFoundStrategy;
import ru.otus.strategies.DeleteEntityStrategy;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Component
public class DeleteEntityCommandImpl implements DeleteEntityCommand{

	private final Map<String, DeleteEntityStrategy> strategyByFieldName;

	public DeleteEntityCommandImpl(@Qualifier("deleteEntityStrategies")
								   final Map<String, DeleteEntityStrategy> strategyByFieldName) {
		this.strategyByFieldName = strategyByFieldName;
	}

	@Override
	public void delete(final String collectionName, final Document source) {
		final Set<Map.Entry<String, Object>> valueByFieldName = source.entrySet();

		for (Map.Entry<String, Object> valueByFieldNameEntry : valueByFieldName) {
			final var fieldName = valueByFieldNameEntry.getKey();
			final var value = valueByFieldNameEntry.getValue();
			final var fieldNameWithCollection = extractFieldName(collectionName, fieldName);
			final var strategy = strategyByFieldName.get(fieldNameWithCollection);

			if (Objects.isNull(strategy)) {
				throw new NotFoundStrategy("strategy for delete book not found!");
			}

			strategy.delete(value.toString());
		}
	}

	private String extractFieldName(final String collectionName, final String fieldName) {
		return collectionName + "." + fieldName;
	}
}
