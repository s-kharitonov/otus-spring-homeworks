package ru.otus.dao.impl;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.dao.QuestionsDAO;
import ru.otus.domain.Question;
import ru.otus.loaders.FileResourceLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class QuestionsDAOImpl implements QuestionsDAO {

	private static final char SEPARATOR = ';';
	private static final Logger logger = LoggerFactory.getLogger(QuestionsDAOImpl.class);

	private final FileResourceLoader resourceLoader;

	public QuestionsDAOImpl(final FileResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	@Override
	public List<Question> findQuestions() {
		final InputStream stream = resourceLoader.loadResource();

		if (stream == null) {
			throw new IllegalArgumentException("resource file does not exist!");
		}

		try (final BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))){
			final CsvToBean<Question> beanBuilder = new CsvToBeanBuilder<Question>(reader)
					.withType(Question.class)
					.withIgnoreEmptyLine(true)
					.withSeparator(SEPARATOR)
					.build();

			return beanBuilder.parse();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			return new ArrayList<>();
		}
	}
}
