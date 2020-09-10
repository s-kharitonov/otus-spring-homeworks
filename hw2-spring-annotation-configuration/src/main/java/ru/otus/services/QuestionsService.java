package ru.otus.services;

import ru.otus.domain.QuestionDTO;

import java.util.List;

public interface QuestionsService {
	List<QuestionDTO> getQuestions();
}
