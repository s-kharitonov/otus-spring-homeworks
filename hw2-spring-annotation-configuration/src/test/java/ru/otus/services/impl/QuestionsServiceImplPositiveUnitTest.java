package ru.otus.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.dao.QuestionsDAO;
import ru.otus.domain.Question;
import ru.otus.services.QuestionsService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class QuestionsServiceImplPositiveUnitTest {

	private QuestionsDAO questionsDAO;
	private QuestionsService questionsService;

	@BeforeEach
	void setUp() {
		questionsDAO = mock(QuestionsDAO.class);
		questionsService = new QuestionsServiceImpl(questionsDAO);
	}

	@Test
	@DisplayName("should return questions")
	void shouldReturnQuestions() {
		given(questionsDAO.findQuestions()).willReturn(Optional.of(List.of(new Question())));
		assertFalse(questionsService.getQuestions().isEmpty());
	}
}