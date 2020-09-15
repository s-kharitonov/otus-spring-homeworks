package ru.otus.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.dao.QuestionsDAO;
import ru.otus.loaders.FileResourceLoader;
import ru.otus.loaders.impl.BasicFileResourceLoader;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class QuestionsDAOCSVUnitTest {

	private final static String PATH_TO_TEST_FILE = "/questions.csv";

	private FileResourceLoader resourceLoader;

	@BeforeEach
	public void beforeEach() {
		resourceLoader = mock(FileResourceLoader.class);
	}

	@Test
	@DisplayName("should throw IllegalArgumentException when input stream is null")
	public void shouldThrownExceptionWhenResourceIsNull() {
		given(resourceLoader.loadResource()).willReturn(null);
		assertThrows(IllegalArgumentException.class, () -> new QuestionsDAOCSV(resourceLoader).findQuestions());
	}

	@Test
	@DisplayName("should return not empty questions list for test file")
	public void shouldReturnNotEmptyList() {
		final FileResourceLoader resourceLoader = new BasicFileResourceLoader(PATH_TO_TEST_FILE);
		final QuestionsDAO questionsDAO = new QuestionsDAOCSV(resourceLoader);

		assertFalse(questionsDAO.findQuestions().isEmpty());
	}
}