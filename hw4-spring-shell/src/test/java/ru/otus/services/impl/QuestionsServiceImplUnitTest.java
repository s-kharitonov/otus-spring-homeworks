package ru.otus.services.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.dao.QuestionsDao;
import ru.otus.domain.Question;
import ru.otus.services.QuestionsService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class QuestionsServiceImplUnitTest {

	@Configuration
	public static class QuestionsServiceImplConfig{
		@Bean
		public QuestionsService questionsService(final QuestionsDao questionsDao) {
			return new QuestionsServiceImpl(questionsDao);
		}
	}

	@MockBean
	private QuestionsDao questionsDao;
	@Autowired
	private QuestionsService questionsService;

	@Test
	@DisplayName("should return questions")
	void shouldReturnQuestions() {
		given(questionsDao.findQuestions()).willReturn(List.of(new Question()));
		assertFalse(questionsService.getQuestions().isEmpty());
	}
}