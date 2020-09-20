package ru.otus.dao.impl;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;
import ru.otus.annotations.Loggable;
import ru.otus.configs.AppProperties;
import ru.otus.dao.QuestionsDao;
import ru.otus.domain.Answer;
import ru.otus.domain.Question;
import ru.otus.exceptions.QuestionsDaoException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class QuestionsDaoCsv implements QuestionsDao {

	private static final char SEPARATOR = ';';

	private final AppProperties appProperties;

	public QuestionsDaoCsv(final AppProperties properties) {
		this.appProperties = properties;
	}

	@Loggable
	@Override
	public List<Question> findQuestions() {
		final String filePath = appProperties.getQuestionsPath();

		try (var stream = new ClassPathResource(filePath).getInputStream();
			 var reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
			final CsvToBean<Question> csvToBean = new CsvToBeanBuilder<Question>(reader)
					.withType(Question.class)
					.withIgnoreEmptyLine(true)
					.withSeparator(SEPARATOR)
					.build();

			final List<Question> questions = csvToBean.parse();

			sortAnswersByNumber(questions);

			return questions;
		} catch (IOException | IllegalStateException e) {
			throw new QuestionsDaoException(e);
		}
	}

	private void sortAnswersByNumber(final List<Question> questions) {
		questions.forEach(question -> {
			var answers = question.getAnswers().stream()
					.sorted(Comparator.comparing(Answer::getNumber))
					.collect(Collectors.toList());

			question.setAnswers(answers);
		});
	}
}
