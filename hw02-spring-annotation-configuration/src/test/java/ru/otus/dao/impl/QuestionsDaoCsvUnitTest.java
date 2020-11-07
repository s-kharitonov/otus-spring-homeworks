package ru.otus.dao.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.dao.QuestionsDao;

import static org.junit.jupiter.api.Assertions.assertFalse;

class QuestionsDaoCsvUnitTest {

	private final static String PATH_TO_TEST_FILE = "/questions.csv";

	@Test
	@DisplayName("should return not empty questions list for test file")
	public void shouldReturnNotEmptyList() {
		final QuestionsDao questionsDao = new QuestionsDaoCsv(PATH_TO_TEST_FILE);
		assertFalse(questionsDao.findQuestions().isEmpty());
	}
}