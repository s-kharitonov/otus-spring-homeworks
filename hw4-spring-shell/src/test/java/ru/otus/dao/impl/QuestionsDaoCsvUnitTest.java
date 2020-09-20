package ru.otus.dao.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.configs.AppProperties;
import ru.otus.dao.QuestionsDao;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class QuestionsDaoCsvUnitTest {

	private final static String PATH_TO_TEST_FILE = "/questions-test.csv";

	@Configuration
	public static class QuestionsDaoCsvConfig {

		@Bean
		public QuestionsDao questionsDao(final AppProperties appProperties) {
			return new QuestionsDaoCsv(appProperties);
		}
	}

	@MockBean
	private AppProperties appProperties;

	@Autowired
	private QuestionsDao questionsDao;

	@Test
	@DisplayName("should return not empty questions list for test file")
	public void shouldReturnNotEmptyList() {
		given(appProperties.getQuestionsPath()).willReturn(PATH_TO_TEST_FILE);
		assertFalse(questionsDao.findQuestions().isEmpty());
	}
}