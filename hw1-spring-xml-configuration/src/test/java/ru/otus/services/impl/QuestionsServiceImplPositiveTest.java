package ru.otus.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.dao.QuestionsDAO;
import ru.otus.domain.Question;
import ru.otus.services.QuestionsService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class QuestionsServiceImplPositiveTest {

	private QuestionsDAO questionsDAO;
	private QuestionsService questionsService;

	@BeforeEach
	void setUp() {
		questionsDAO = mock(QuestionsDAO.class);
		questionsService = new QuestionsServiceImpl(questionsDAO);
	}

	@Test
	void shouldReturnNotEmptyList() {
		when(questionsDAO.findQuestions()).thenReturn(List.of(new Question()));
		assertEquals(questionsService.getQuestions().size(), 1);
	}
}