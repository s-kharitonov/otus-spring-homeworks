package ru.otus.services;

import ru.otus.domain.Question;

import java.util.List;

public interface QuestionsService {
	List<Question> getQuestions();
}
