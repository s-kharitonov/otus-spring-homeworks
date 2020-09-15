package ru.otus.dao.impl;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;
import ru.otus.annotations.Loggable;
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

	private final String path;

	public QuestionsDaoCsv(@Value("${questions.file.path}")final String path) {
		this.path = path;
	}

	@Loggable
	@Override
	public List<Question> findQuestions() {
		try (var stream = new ClassPathResource(path).getInputStream();
			 var reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
			final CsvToBean<Question> csvToBean = new CsvToBeanBuilder<Question>(reader)
					.withType(Question.class)
					.withIgnoreEmptyLine(true)
					.withSeparator(SEPARATOR)
					.build();

			final List<Question> questions = csvToBean.parse();

			questions.forEach(question -> {
				var answers = question.getAnswers().stream()
						.sorted(Comparator.comparing(Answer::getNumber))
						.collect(Collectors.toList());

				question.setAnswers(answers);
			});

			return questions;
		} catch (IOException e) {
			throw new QuestionsDaoException(e);
		}
	}
}
