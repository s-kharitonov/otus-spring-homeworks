package ru.otus.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.dao.QuestionsDao;
import ru.otus.domain.Question;
import ru.otus.services.QuestionsService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class QuestionsServiceImplUnitTest {

	private QuestionsDao questionsDao;
	private QuestionsService questionsService;

	@BeforeEach
	void setUp() {
		questionsDao = mock(QuestionsDao.class);
		questionsService = new QuestionsServiceImpl(questionsDao);
	}

	@Test
	@DisplayName("should return questions")
	void shouldReturnQuestions() {
		given(questionsDao.findQuestions()).willReturn(List.of(new Question()));
		assertFalse(questionsService.getQuestions().isEmpty());
	}
}