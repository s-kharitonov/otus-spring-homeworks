package ru.otus.services.impl;

import org.junit.jupiter.api.Test;
import ru.otus.domain.Question;
import ru.otus.loaders.impl.BasicFileResourceLoader;
import ru.otus.services.QuestionsService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class QuestionsServiceImplPositiveTest {

	private final static String PATH_TO_TEST_FILE = "/questions.csv";

	@Test
	void shouldReturnNotEmptyList() {
		final QuestionsService questionsService = new QuestionsServiceImpl(new BasicFileResourceLoader(PATH_TO_TEST_FILE));
		final List<Question> questions = questionsService.getQuestions();
		assertTrue(questions.size() > 0);
	}
}