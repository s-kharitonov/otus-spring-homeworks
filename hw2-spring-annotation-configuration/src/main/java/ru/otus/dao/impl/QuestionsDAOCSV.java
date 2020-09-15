package ru.otus.dao.impl;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.otus.annotations.Loggable;
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

@Component
public class QuestionsDAOCSV implements QuestionsDAO {

	private static final char SEPARATOR = ';';
	private static final Logger logger = LoggerFactory.getLogger(QuestionsDAOCSV.class);

	private final FileResourceLoader resourceLoader;

	public QuestionsDAOCSV(final FileResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	@Loggable
	@Override
	public List<Question> findQuestions() {
		final InputStream stream = resourceLoader.loadResource();

		if (stream == null) {
			throw new IllegalArgumentException("resource file does not exist!");
		}

		try (final BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))){
			final CsvToBean<Question> csvToBean = new CsvToBeanBuilder<Question>(reader)
					.withType(Question.class)
					.withIgnoreEmptyLine(true)
					.withSeparator(SEPARATOR)
					.build();

			return csvToBean.parse();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			return new ArrayList<>();
		}
	}
}
