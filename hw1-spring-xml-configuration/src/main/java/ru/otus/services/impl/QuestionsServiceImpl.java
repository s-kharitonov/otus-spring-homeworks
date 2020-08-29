package ru.otus.services.impl;

import ru.otus.domain.Answer;
import ru.otus.domain.Question;
import ru.otus.loaders.FileResourceLoader;
import ru.otus.readers.FileReader;
import ru.otus.readers.impl.BasicFileReader;
import ru.otus.services.QuestionsService;
import ru.otus.utils.StringUtils;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

public class QuestionsServiceImpl implements QuestionsService {

	private final static String DELIMITER = ";";
	private final static int FIRST_ANSWERS_INDEX = 1;
	private final static int QUESTION_INDEX = 0;

	private final FileResourceLoader resourceLoader;

	public QuestionsServiceImpl(final FileResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	@Override
	public List<Question> getQuestions() {
		final InputStream stream = resourceLoader.loadResource();

		if (stream == null) {
			throw new IllegalArgumentException("resource file does not exist!");
		}

		final FileReader fileReader = new BasicFileReader(stream);
		final List<String> lines = fileReader.readLines();

		if (lines == null || lines.isEmpty()) {
			throw new IllegalArgumentException("file is empty!");
		}

		return createQuestions(lines);
	}

	private List<Question> createQuestions(final List<String> lines) {
		return lines.stream()
				.filter(line -> line.contains(DELIMITER))
				.map(line -> StringUtils.splitString(line, DELIMITER))
				.map(this::createQuestion)
				.collect(Collectors.toList());
	}

	private List<String> extractAnswersElements(final List<String> lineElements) {
		return lineElements.subList(FIRST_ANSWERS_INDEX, lineElements.size());
	}

	private Question createQuestion(final List<String> lineElements) {
		final List<String> answersLineElements = extractAnswersElements(lineElements);
		final List<Answer> answers = createAnswers(answersLineElements);

		return new Question(lineElements.get(QUESTION_INDEX), answers);
	}

	private List<Answer> createAnswers(final List<String> answers) {
		return answers.stream().map(Answer::new).collect(Collectors.toList());
	}
}
