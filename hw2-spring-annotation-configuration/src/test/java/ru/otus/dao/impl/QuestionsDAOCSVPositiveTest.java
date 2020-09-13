package ru.otus.dao.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.dao.QuestionsDAO;
import ru.otus.loaders.FileResourceLoader;
import ru.otus.loaders.impl.BasicFileResourceLoader;

import static org.junit.jupiter.api.Assertions.assertTrue;

class QuestionsDAOCSVPositiveTest {

	private final static String PATH_TO_TEST_FILE = "/questions.csv";

	@Test
	@DisplayName("should return not empty questions list for test file")
	public void shouldReturnNotEmptyList() {
		final FileResourceLoader resourceLoader = new BasicFileResourceLoader(PATH_TO_TEST_FILE);
		final QuestionsDAO questionsDAO = new QuestionsDAOCSV(resourceLoader);

		assertTrue(questionsDAO.findQuestions().isPresent());
	}
}